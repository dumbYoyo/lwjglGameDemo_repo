#version 330

in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
    fragColor = vec4(vec3(texture(texture_sampler, outTexCoord).r), 1.0);
}