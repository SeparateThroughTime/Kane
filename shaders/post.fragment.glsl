#version 330 core

in vec2 texCoords;

uniform sampler2D textureID;
uniform float time;
uniform vec2 resolution;
uniform float sanity;

out vec4 color;

// Random Boolean.
// theshold determines the possibility for true.
// invertedDuration the time it stays true/false in seconds.
bool randomBool(float threshold, float invertedDuration) {
    return step(fract(sin(floor(time * invertedDuration) * 100002.423458)), threshold) == 1.0 ? true : false;
}

vec4 darkening() {
    return vec4(vec3(min(1, abs(sin(time * 0.25) * cos(time * 0.1)) + 1 - sanity * 0.8)), 1.0);
}

vec4 flickering() {
    int timeMult = 5;

    if (randomBool(1.000001 - pow(sanity, 2) * 0.05, timeMult)) {
        return vec4(0.0);
    }

    float impulse = fract(time * timeMult) * 8.0;
    return vec4(vec3(impulse * exp(1.0 - impulse)), 0.0);
}

// https://www.geeks3d.com/20110428/shader-library-swirl-post-processing-filter-in-glsl/
vec2 swirl(vec2 uv, vec2 center, float radius, float angle, float timeVar1, float timeVar2) {
    uv -= center;
    float dist = length(uv);

    if (dist < radius) {
        float percent = (radius - dist) / radius;
        float theta = percent * percent * angle * 8.0 * cos(time * timeVar1) * sin(time * timeVar2) * sanity;
        float s = sin(theta);
        float c = cos(theta);
        uv = vec2(dot(uv, vec2(c, -s)), dot(uv, vec2(s, c)));
    }

    uv += center;
    return uv;
}

void main() {
    vec2 uv = texCoords.st;

    uv = swirl(uv, vec2(0.4, 0.2), 0.1, 0.15, 0.08, 0.05);
    uv = swirl(uv, vec2(0.6, 0.35), 0.7, 0.1, 0.03, 0.02);
    uv = swirl(uv, vec2(0.24, 0.76), 0.2, 0.14, 0.07, 0.05);
    uv = swirl(uv, vec2(0.8, 0.6), 0.25, 0.12, 0.034, 0.008);
    uv = swirl(uv, vec2(0.44, 0.72), 0.6, 0.06, 0.01, 0.023);

    color = texture(textureID, uv) * darkening();
    color += vec4(vec3(flickering() * color.a), 0.0);
}