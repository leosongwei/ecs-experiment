#version 450 core
layout(location = 0) in vec2 modelCoord;
layout(location = 1) in vec2 texCoord;
out vec2 fragTexCoord;
void main(){
    fragTexCoord = texCoord;
    gl_Position.xy = modelCoord;
    gl_Position.z = 0.0;
    gl_Position.w = 1.0;
}