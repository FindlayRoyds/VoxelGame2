#version 410

#define ARRAY_SIZE 4
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

uniform vec3 vertexDataArray[108];
uniform vec2 textureDataArray[108];
uniform int textureIndexArray[108];
uniform vec3 normalDataArray[108];

uniform ivec3 chunkPosition;

vec3 sunDirection = normalize(vec3(1, 3, 2));

void main()
{
    int blockVertexID = inputData & 0xFF; // Extract the lowest 8 bits
    int blockType = (inputData >> 8) & 0xFF; // Extract the next 8 bits
    int blockPosition = inputData >> 16; // Extract the highest 16 bits

    int vertexDataIndex = blockType * 36 + blockVertexID;
    vec3 pos = vertexDataArray[vertexDataIndex];
    texCoord = textureDataArray[vertexDataIndex];
    normal = normalDataArray[vertexDataIndex];
    texIndexFloat = float(textureIndexArray[vertexDataIndex]);
    vec3 offset = vec3(int(blockPosition / (CHUNK_SIZE * CHUNK_SIZE)), int((blockPosition / CHUNK_SIZE)) % CHUNK_SIZE, blockPosition % CHUNK_SIZE);
    vec3 worldPos = pos + offset + WORLD_OFFSET + chunkPosition * CHUNK_SIZE;
    vec4 viewPos = viewMatrix * vec4(worldPos, 1.0);
    viewDir = -viewPos.xyz;
    gl_Position = projectionMatrix * viewMatrix * vec4(worldPos, 1.0);
//    outColor = vec3(0.6, 0.23, 0.05);
//    if (int(blockVertexID / 6) % 6 == 4 || int(blockVertexID / 6) % 6 == 5) {
//        outColor = vec3(0.1, 0.75, 0.1);
//    }

    // outColor = vec3(float(int(blockVertexID / 6) % 6) / 6, float(int((blockVertexID) / 6 + 2) % 6)  / 6, float(int((blockVertexID) / 6 + 4) % 6) / 6);
}
