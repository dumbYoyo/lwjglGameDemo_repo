#version 330

in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main() {
    vec4 texColor = texture(texture_sampler, outTexCoord);
    if (texColor.a < 0.1)
        discard;
    fragColor = texColor;
}