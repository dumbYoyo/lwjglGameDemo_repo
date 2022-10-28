#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTextureCoordinates;
layout (location = 2) in vec3 aNormal;

out vec2 textureCoordinates;
out vec3 normal;
out vec3 vertexPos;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;

uniform vec3 camPos;

void main() {
    gl_Position = proj * view * model * vec4(aPos, 1.0);
    textureCoordinates = aTextureCoordinates;
    normal = normalize(model * vec4(aNormal, 0.0)).xyz; // we set w component to be 0, because we dont want to tranlate it
    vertexPos = (model * vec4(aPos, 1.0)).xyz;
}