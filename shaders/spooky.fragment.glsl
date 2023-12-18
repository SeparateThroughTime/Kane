#version 330 core


in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];
uniform float time;
uniform vec2 resolution;

out vec4 color;

// Random Boolean.
// theshold determines the possibility for true.
// invertedDuration the time it stays true/false in seconds.
bool randomBool(float threshold, float invertedDuration) {
    return step(fract(sin(floor(time * invertedDuration) * 100002.423458)), threshold) == 1.0 ? true : false;
}

vec4 darkening() {
    return vec4(vec3(min(1, abs(sin(time * 0.25) * cos(time * 0.1)) + 0.25)), 1.0);
}

vec4 flickering() {
    float fastTime = time * 5;
    float fractFastTime = fract(fastTime);
    float floorFastTime = floor(fastTime);

    if (randomBool(0.95, 5)) {
        return vec4(0.0);
    }

    float impulse = fractFastTime * 8.0;
    return vec4(vec3(impulse * exp(1.0 - impulse)), 0.0);
}

void main() {
    if (fTexId > 0) {
        int id = int(fTexId);
        color = texture(uTextures[id], fTexCoords) * darkening();
        color += vec4(vec3(flickering() * color.a), 0.0);
    } else {
        color = fColor;
    }
}