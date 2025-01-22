#version 410

// in vec3 outColor;

out vec4 fragColor;

in vec2 texCoord;
in float texIndexFloat;
in vec3 normal;
in vec3 viewDir;
in float lightValueOut;

uniform sampler2DArray textureArray;
uniform float chunkVisibility;

vec3 shadowValue = vec3(0.65, 0.65, 0.62);
vec3 lightValue = vec3(1, 1, 1);
vec3 sunDirection = normalize(vec3(1, 3, 2));
float shininess = 32.0;

void main()
{
    int texIndex = int(texIndexFloat);
    vec3 texCoord3D = vec3(texCoord, texIndex);
    vec4 originalColor = texture(textureArray, texCoord3D);

    if (originalColor.a < 0.5)
        discard;

    float aoBrightness = min(lightValueOut, 1) * 0.3 + (1 - 0.3);

    float brightness = max(-0.1, dot(normal, sunDirection));
    vec3 scaledBrightness = ((lightValue - shadowValue) * brightness + shadowValue) * aoBrightness;
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

    fragColor = mix(vec4(0.5, 0.7, 1.0, 1.0), fragColor, chunkVisibility);
}