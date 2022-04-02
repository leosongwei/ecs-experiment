#version 460 core
uniform sampler2D TEXTURE1;

in vec2 fragTexCoord;
out vec4 color;
void main(){
    vec4 texColor = texture(TEXTURE1, fragTexCoord);
    //if(texColor.a < 0.01) discard;
    color = texColor;
}