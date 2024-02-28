#version 410

#define ARRAY_SIZE 4

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
    vec3 offset = vec3(int(blockPosition / (32 * 32)), int((blockPosition / 32)) % 32, blockPosition % 32);

    gl_Position = projectionMatrix * viewMatrix * vec4(pos + offset + chunkPosition * 32, 1.0);
    outColor = vec3(float(int(blockVertexID / 6) % 6) / 6, float(int((blockVertexID) / 6 + 2) % 6)  / 6, float(int((blockVertexID) / 6 + 4) % 6) / 6);
}
