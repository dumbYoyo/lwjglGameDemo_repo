#version 330 core

struct Material {
    float specularStrength;
    float reflectivity;
    vec3 tint;
    vec3 color;
};

in vec2 textureCoordinates;
in vec3 normal;
in vec3 vertexPos;
in vec3 camDir;
in vec4 fragPosLightSpace;

out vec4 aColour;

uniform sampler2D tex_sampler;
uniform sampler2D shadow_sampler;

uniform vec3 lightPos;
uniform vec3 lightColor;
uniform vec3 direction;

uniform Material material;

float shadowCalculations(vec4 fragPos) {
    vec3 projCoords = fragPos.xzy / fragPos.w; // convert to NDC [-1, 1], perspective division
    projCoords = projCoords * 0.5 + 0.5; // transform [-1,1] to [0, 1] as depthMap is in range [0, 1]

    float closestDepth = texture(shadow_sampler, projCoords.xy).r;
    float currentDepth = projCoords.z;
    float shadow = currentDepth > closestDepth ? 1.0 : 0.0;

    return shadow;
}

void main() {
    vec3 toLight = normalize(-direction);
    float diff = max(dot(toLight, normal), 0.1f);
    vec3 diffuse = diff * lightColor;

    vec3 fromLight = -(toLight);
    vec3 reflectedLight = normalize(reflect(fromLight, normal));
    float spec = max(dot(normalize(camDir), reflectedLight), 0);
    spec = pow(spec, material.specularStrength);
    vec3 specular = spec * material.reflectivity * lightColor;

    float shadow = shadowCalculations(fragPosLightSpace);

    if (material.color == vec3(0)) {
        aColour = texture(tex_sampler, textureCoordinates) * ((vec4(diffuse, 1.0) + vec4(specular, 1.0)) * (1.0 - shadow));
    } else {
        aColour = vec4(material.color, 1.0) * ((vec4(material.tint, 1.0) * vec4(diffuse, 1.0) + vec4(specular, 1.0)) * (1.0 - shadow));
    }
}