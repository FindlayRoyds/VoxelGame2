#version 410

#define BLOCK_DATA_SIZE 36
#define ARRAY_SIZE 7
#define CHUNK_SIZE 32
#define WORLD_OFFSET vec3(0.5, 0.5, 0.5)

//layout (location=0) in int blockVertexID;
//layout (location=1) in int blockPosition;
//layout (location=2) in int blockType;
layout (location=0) in int inputData;
layout (location=1) in float lightValueIn;

// out vec3 outColor;
out vec2 texCoord;
out float texIndexFloat;
out vec3 normal;
out vec3 viewDir;
out float lightValueOut;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

//uniform vec3 vertexPositionArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
//uniform vec2 textureCoordArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
//uniform int textureIndexArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
//uniform vec3 normalVectorArray[ARRAY_SIZE * BLOCK_DATA_SIZE];

layout(std140) uniform VertexPositionBlock {
    vec3 vertexPositionArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
};

layout(std140) uniform TextureCoordBlock {
    vec2 textureCoordArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
};

layout(std140) uniform TextureIndexBlock {
    int textureIndexArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
};

layout(std140) uniform NormalVectorBlock {
    vec3 normalVectorArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
};

uniform ivec3 chunkPosition;
uniform float chunkVisibility;

uniform float time;

vec3 sunDirection = normalize(vec3(1, 3, 2));


vec3 randomVec3FromBlockPosition(vec3 index) {
    float seed = index.x + index.y * 32 + index.z * 32*32;

    float rand1 = fract(sin(seed * 12.9898) * 43758.5453);
    float rand2 = fract(sin(seed + 3 * 7.9898) * 34753.7453);
    float rand3 = fract(sin(seed + 7 * 15.7898) * 64457.2453);

    return vec3(rand1, rand2, rand3);
}

float randomFloatFromBlockPosition(vec3 index) {
    float seed = index.x + index.y * 32 + index.z * 32*32;
    return fract(sin(seed * 12.9898) * 43758.5453);
}


void main()
{
    int blockVertexID = inputData & 0xFF; // Extract the lowest 8 bits
    int blockType = (inputData >> 8) & 0xF; // Extract the next 4 bits
    int blockIndex = inputData >> 12; // Extract the highest 20 bits

    int vertexDataIndex = blockType * BLOCK_DATA_SIZE + blockVertexID;
    vec3 pos = vertexPositionArray[vertexDataIndex];
    texCoord = textureCoordArray[vertexDataIndex];
    normal = normalVectorArray[vertexDataIndex];
    texIndexFloat = float(textureIndexArray[vertexDataIndex]) + 0.5;
    vec3 blockPosition = vec3(int(blockIndex / (CHUNK_SIZE * CHUNK_SIZE)), int((blockIndex / CHUNK_SIZE)) % CHUNK_SIZE, blockIndex % CHUNK_SIZE);
    vec3 worldBlockPosition = blockPosition + WORLD_OFFSET + chunkPosition * CHUNK_SIZE;

    switch (blockType) {
        case 5: // grass
            float randomRotation = randomFloatFromBlockPosition(worldBlockPosition) * 2 * 3.1415;
            mat3 rotationMatrix = mat3(
                vec3(cos(randomRotation), 0, sin(randomRotation)),
                vec3(0, 1, 0),
                vec3(-sin(randomRotation), 0, cos(randomRotation))
            );
            pos = rotationMatrix * pos;
            normal = rotationMatrix * normal;

            pos += ((pos.y + 0.5) * vec3(0.2 * sin(time * 2 + (worldBlockPosition.x - worldBlockPosition.z) * 0.2), 0, 0.2 * cos(time * 1.7 + (worldBlockPosition.x - worldBlockPosition.z) * 0.2))) * sin(time / 4) * (1.2 + 0.3);
            pos += (randomVec3FromBlockPosition(worldBlockPosition) - vec3(0.5, 0, 0.5)) * vec3(0.5, 0, 0.5);
            break;
        default:
            break;
    }
//    pos += vec3(0, 2 * (3 * sin(time * 0.6 + ((pos + worldBlockPosition).x - (pos + worldBlockPosition).z + (pos + worldBlockPosition).y) * 0.04) - 0.5), 0);

    vec3 worldPos = pos + worldBlockPosition;
    vec4 viewPos = viewMatrix * vec4(worldPos, 1.0);
    viewDir = -viewPos.xyz;

    gl_Position = projectionMatrix * viewMatrix * vec4(worldPos, 1.0);

    lightValueOut = lightValueIn;
}
