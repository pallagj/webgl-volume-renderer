#version 300 es
in vec4 vertexPosition;
in vec2 vertexTexCoord;

out vec2 tex;
out vec4 rayDir;

uniform struct {
  vec3 position;
  mat4 rayDirMatrix;
  vec3 up;
  vec3 right;
} camera;

void main(void) {
  tex = vertexTexCoord;
  rayDir = vertexPosition * camera.rayDirMatrix;
  gl_Position = vertexPosition;
}
