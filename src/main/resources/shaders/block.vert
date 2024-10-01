#version 410

#define BLOCK_DATA_SIZE 42
#define ARRAY_SIZE 5
#define CHUNK_SIZE 32
#define WORLD_OFFSET vec3(0.5, 0.5, 0.5)

//layout (location=0) in int blockVertexID;
//layout (location=1) in int blockPosition;
//layout (location=2) in int blockType;
layout (location=0) in int inputData;

// out vec3 outColor;
out vec2 texCoord;
out float texIndexFloat;
out vec3 normal;
out vec3 viewDir;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

uniform vec3 vertexPositionArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
uniform vec2 textureCoordArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
uniform int textureIndexArray[ARRAY_SIZE * BLOCK_DATA_SIZE];
uniform vec3 normalVectorArray[ARRAY_SIZE * BLOCK_DATA_SIZE];

uniform ivec3 chunkPosition;
uniform float chunkVisibility;

uniform float time;

vec3 sunDirection = normalize(vec3(1, 3, 2));

void main()
{
    int blockVertexID = inputData & 0xFF; // Extract the lowest 8 bits
    int blockType = (inputData >> 8) & 0xF; // Extract the next 4 bits
    int blockPosition = inputData >> 12; // Extract the highest 20 bits

    int vertexDataIndex = blockType * BLOCK_DATA_SIZE + blockVertexID;
    vec3 pos = vertexPositionArray[vertexDataIndex];
    texCoord = textureCoordArray[vertexDataIndex];
    normal = normalVectorArray[vertexDataIndex];
    texIndexFloat = float(textureIndexArray[vertexDataIndex]);
    vec3 offset = vec3(int(blockPosition / (CHUNK_SIZE * CHUNK_SIZE)), int((blockPosition / CHUNK_SIZE)) % CHUNK_SIZE, blockPosition % CHUNK_SIZE);
    vec3 worldPos = pos + offset + WORLD_OFFSET + chunkPosition * CHUNK_SIZE;
    vec4 viewPos = viewMatrix * vec4(worldPos, 1.0);
    viewDir = -viewPos.xyz;

    switch (blockType) {
        case 3:
            worldPos += vec3(0, 0.1 * (0.5 * sin(time * 5 + worldPos.x - worldPos.z) - 0.5), 0);
            break;
        default:
            break;
    }

    gl_Position = projectionMatrix * viewMatrix * vec4(worldPos, 1.0);

//    gl_Position = gl_Position - vec4(0, (1 - chunkVisibility) * 32, 0, 0);
//    outColor = vec3(0.6, 0.23, 0.05);
//    if (int(blockVertexID / 6) % 6 == 4 || int(blockVertexID / 6) % 6 == 5) {
//        outColor = vec3(0.1, 0.75, 0.1);
//    }

    // outColor = vec3(float(int(blockVertexID / 6) % 6) / 6, float(int((blockVertexID) / 6 + 2) % 6)  / 6, float(int((blockVertexID) / 6 + 4) % 6) / 6);
}
