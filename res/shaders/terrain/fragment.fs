#version 330 core

in vec2 textureCoordinates;
in vec3 normal;
in vec3 vertexPos;
in vec3 camDir;

out vec4 aColour;

uniform sampler2D tex_sampler;

uniform vec3 lightPos;
uniform vec3 lightColor;

void main() {
    vec3 toLight = normalize(lightPos - vertexPos);
    float diff = dot(toLight, normal);
    vec3 diffuse = diff * lightColor;

    aColour = (texture(tex_sampler, textureCoordinates * 40) * vec4(diffuse, 1.0));
}