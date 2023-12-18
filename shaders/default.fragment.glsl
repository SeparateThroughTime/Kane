#version 330 core


in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 color;

void main() {
    if (fTexId > 0) {
        int id = int(fTexId);
        color = texture(uTextures[id], fTexCoords);
    } else {
        color = fColor;
    }
}