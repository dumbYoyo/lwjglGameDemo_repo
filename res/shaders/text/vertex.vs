#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;

out vec2 outTexCoord;

uniform mat4 proj;
uniform mat4 model;

void main() {
    gl_Position = proj * model * vec4(position, 1.0);
    outTexCoord = texCoord;
}