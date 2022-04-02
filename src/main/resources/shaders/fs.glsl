#version 330 core
uniform sampler2D quadTexture;
in vec2 fragTexCoord;
out vec3 color;
void main(){
    color = vec3(texture(quadTexture, fragTexCoord));
}