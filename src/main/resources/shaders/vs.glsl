#version 460 core
layout(location = 0) in vec2 vertexCoord;
layout(location = 1) in vec2 texCoord;
layout(location = 2) in int _textureCode;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;

out int textureCode;
out vec2 fragTexCoord;

void main(){
    fragTexCoord = texCoord;
    vec4 vertex = model * vec4(vertexCoord.x, vertexCoord.y, 0.0, 1.0);
    gl_Position = projection * view * vertex;
    textureCode = _textureCode;
}