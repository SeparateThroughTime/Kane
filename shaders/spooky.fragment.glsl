#version 330 core


in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];
uniform float time;
uniform vec2 resolution;

out vec4 color;

vec4 darkening() {
    return vec4(vec3(min(1, abs(sin(time * 0.25) * cos(time * 0.1)) + 0.25)), 1.0);
}

vec4 flickering() {
    float fastTime = time * 5;
    float fractFastTime = fract(fastTime);
    float floorFastTime = floor(fastTime);

    //    if(!(floorFastTime % 2 == 0 && floorFastTime % 5 == 0 && floorFastTime % 7 == 0)) {
    //        return 0.0;
    //    }

    float impulse = fractFastTime * 8.0;
    return vec4(vec3(impulse * exp(1.0 - impulse)), 1.0);
}

void main() {
    if (fTexId > 0) {
        int id = int(fTexId);
        color = texture(uTextures[id], fTexCoords) * darkening();
        color = texture(uTextures[id], fTexCoords);
        color += vec4(vec3(flickering() * color.a), 0.0);
    } else {
        color = fColor;
    }
}