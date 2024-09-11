#version 330 core

in vec2 texCoords;

out vec4 color;

uniform sampler2D textureID;

void main() {
    color = texture(textureID, texCoords);
}