#version 410

#define ARRAY_SIZE 4
#define CHUNK_SIZE 32
#define WORLD_OFFSET vec3(0.5, 0.5, 0.5)

layout (location=0) in int blockVertexID;
layout (location=1) in int blockPosition;
layout (location=2) in int blockType;

// out vec3 outColor;
out vec2 texCoord;
out float texIndexFloat;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 vertexDataArray[108];
uniform vec2 textureDataArray[108];
uniform int textureIndexArray[108];
uniform ivec3 chunkPosition;

void main()
{
    int vertexDataIndex = blockType * 36 + blockVertexID;
    vec3 pos = vertexDataArray[vertexDataIndex];
    texCoord = textureDataArray[vertexDataIndex];
    texIndexFloat = float(textureIndexArray[vertexDataIndex]);
    vec3 offset = vec3(int(blockPosition / (CHUNK_SIZE * CHUNK_SIZE)), int((blockPosition / CHUNK_SIZE)) % CHUNK_SIZE, blockPosition % CHUNK_SIZE);
    vec3 worldPos = pos + offset + WORLD_OFFSET + chunkPosition * 32;
    gl_Position = projectionMatrix * viewMatrix * vec4(worldPos, 1.0);
//    outColor = vec3(0.6, 0.23, 0.05);
//    if (int(blockVertexID / 6) % 6 == 4 || int(blockVertexID / 6) % 6 == 5) {
//        outColor = vec3(0.1, 0.75, 0.1);
//    }

    // outColor = vec3(float(int(blockVertexID / 6) % 6) / 6, float(int((blockVertexID) / 6 + 2) % 6)  / 6, float(int((blockVertexID) / 6 + 4) % 6) / 6);
}
