#version 330

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

uniform vec3 color;
uniform vec3 tint;

void main() {
    if (color == vec3(0)) {
        fragColor = texture(texture_sampler, texCoord);
    } else {
        fragColor = vec4(color, 1.0);
    }
}