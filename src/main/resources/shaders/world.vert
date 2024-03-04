#version 410

#define ARRAY_SIZE 4
#define CHUNK_SIZE 32
#define WORLD_OFFSET vec3(0.5, 0.5, 0.5)

layout (location=0) in int blockVertexID;
layout (location=1) in int blockPosition;

out vec3 outColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 vertexDataArray[36];
uniform ivec3 chunkPosition;

void main()
{
    vec3 pos = vertexDataArray[blockVertexID];
    vec3 offset = vec3(int(blockPosition / (CHUNK_SIZE * CHUNK_SIZE)), int((blockPosition / CHUNK_SIZE)) % CHUNK_SIZE, blockPosition % CHUNK_SIZE);

    gl_Position = projectionMatrix * viewMatrix * vec4(pos + offset + WORLD_OFFSET + chunkPosition * 32, 1.0);
    outColor = vec3(float(int(blockVertexID / 6) % 6) / 6, float(int((blockVertexID) / 6 + 2) % 6)  / 6, float(int((blockVertexID) / 6 + 4) % 6) / 6);
}
