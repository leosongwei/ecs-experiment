#version 460 core
uniform sampler2DArray TEXTURE2;

flat in int textureCode;
in vec2 fragTexCoord;

out vec4 color;

void main(){
    vec4 texColor = texture(TEXTURE2, vec3(fragTexCoord, textureCode));
    color = texColor;
}