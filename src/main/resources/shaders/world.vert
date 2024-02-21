#version 410

layout (location=0) in vec3 position;

out vec3 outColor;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
    outColor = vec3((gl_VertexID % 3) / 2, ((gl_VertexID + 1) % 3) / 2, ((gl_VertexID + 2) % 3) / 2);
}