#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 InSize;

in vec2 texCoord;
in vec2 sampleStep;

uniform float Radius;
uniform float RadiusMultiplier;

out vec4 fragColor;

void main() {
    vec2 center = 0.5*InSize;
    vec2 pixel = texCoord*InSize;
// * InSize / max(InSize.x, InSize.y)
    if (distance(pixel, center)/max(InSize.x, InSize.y) > 0.28) {
        vec4 blurred = vec4(0.0);
        float actualRadius = round(Radius * RadiusMultiplier);
        for (float a = -actualRadius + 0.5; a <= actualRadius; a += 2.0) {
            blurred += texture(DiffuseSampler, texCoord + sampleStep * a);
        }
        blurred += texture(DiffuseSampler, texCoord + sampleStep * actualRadius) / 2.0;
        fragColor = blurred / (actualRadius + 0.5);
    } else {
        fragColor = texture(DiffuseSampler, texCoord);
    }
}
