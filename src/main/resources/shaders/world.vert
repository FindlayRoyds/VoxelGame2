#version 410

#define ARRAY_SIZE 4

layout (location=0) in int blockVertexID;

out vec3 outColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 vertexDataArray[4];

void main()
{
    vec3 pos = vertexDataArray[blockVertexID];

    gl_Position = projectionMatrix * viewMatrix * vec4(pos, 1.0);
    outColor = vec3((gl_VertexID % 3) / 2, ((gl_VertexID + 1) % 3) / 2, ((gl_VertexID + 2) % 3) / 2);
}
