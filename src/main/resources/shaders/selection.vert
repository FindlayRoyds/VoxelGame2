#version 410 core

layout (location = 0) in vec3 aPos;

uniform ivec3 selectionBoxPosition;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * viewMatrix * vec4(aPos + selectionBoxPosition + vec3(0.5, 0.5, 0.5), 1.0);
}