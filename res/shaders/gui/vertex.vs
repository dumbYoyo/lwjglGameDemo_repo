#version 330

layout (location=0) in vec3 aPos;
layout (location=1) in vec2 aTexCoord;

out vec2 texCoord;

uniform mat4 proj;
uniform mat4 model;

void main() {
    gl_Position = proj * model * vec4(aPos, 1.0);
    texCoord = aTexCoord;
}