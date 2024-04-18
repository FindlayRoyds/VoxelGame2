#version 410

// in vec3 outColor;

out vec4 fragColor;

in vec2 texCoord;
in float texIndexFloat;

uniform sampler2DArray textureArray;

void main()
{
    int texIndex = int(texIndexFloat);
    vec3 texCoord3D = vec3(texCoord, texIndex);
    fragColor = texture(textureArray, texCoord3D);
}