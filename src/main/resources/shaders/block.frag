#version 410

// in vec3 outColor;

out vec4 fragColor;

in vec2 texCoord;
in float texIndexFloat;
in vec3 normal;
in vec3 viewDir;

uniform sampler2DArray textureArray;

vec3 shadowValue = vec3(0.65, 0.65, 0.75);
vec3 lightValue = vec3(1.1, 1.1, 1.0);
vec3 sunDirection = normalize(vec3(1, 3, 2));
float shininess = 32.0;

void main()
{
    int texIndex = int(texIndexFloat);
    vec3 texCoord3D = vec3(texCoord, texIndex);
    vec4 originalColor = texture(textureArray, texCoord3D);

    float brightness = max(0, dot(normal, sunDirection));
    vec3 scaledBrightness = (lightValue - shadowValue) * brightness + shadowValue;
//
//    // Calculate the reflection direction
//    vec3 reflectDir = reflect(-sunDirection, normal);
//
//    // Calculate the specular reflection
//    float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
//
//    // Add the specular reflection to the color
//    vec4 specularColor = vec4(1.0, 1.0, 1.0, 1.0) * spec;

    vec4 modifiedColor = vec4(originalColor.rgb * scaledBrightness, originalColor.a);// + specularColor;
    fragColor = modifiedColor;

    if (fragColor.a == 0.0)
        discard;
}