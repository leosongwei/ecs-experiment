#version 460 core
uniform sampler2D TEXTURE1;

flat in int textureRow;
flat in int textureCol;
in vec2 fragTexCoord;

out vec4 color;

vec2 atlasCoord() {
    float x = mod(fragTexCoord.x, 1.0);
    float y = mod(fragTexCoord.y, 1.0);
    float size = 1f/8f;
    float baseX = textureCol * size;
    float baseY = (7 - textureRow) * size;
    return vec2(
        baseX + fragTexCoord.x * size,
        baseY + fragTexCoord.y * size
    );
}

void main(){
    vec4 texColor = texture(TEXTURE1, atlasCoord());
    color = texColor;
}