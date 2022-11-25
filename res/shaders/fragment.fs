#version 330 core

struct Material {
    float specularStrength;
    float reflectivity;
};

in vec2 textureCoordinates;
in vec3 normal;
in vec3 vertexPos;
in vec3 camDir;

out vec4 aColour;

uniform sampler2D tex_sampler;

uniform vec3 lightPos;
uniform vec3 lightColor;

uniform Material material;

void main() {
    vec3 toLight = normalize(lightPos - vertexPos);
    float diff = max(dot(toLight, normal), 0.1f);
    vec3 diffuse = diff * lightColor;

    vec3 fromLight = -(toLight);
    vec3 reflectedLight = normalize(reflect(fromLight, normal));
    float spec = max(dot(normalize(camDir), reflectedLight), 0);
    spec = pow(spec, material.specularStrength);
    vec3 specular = spec * material.reflectivity * lightColor;

    //aColour = texture(tex_sampler, textureCoordinates);
    aColour = (texture(tex_sampler, textureCoordinates) * vec4(diffuse, 1.0) + vec4(specular, 1.0));
    //aColour = vec4(1, 1, 1, 1);
}