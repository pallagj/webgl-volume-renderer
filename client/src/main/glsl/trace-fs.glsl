#version 300 es 
precision highp float;

uniform struct {
  vec3 position;
  mat4 rayDirMatrix;
  vec3 up;
  vec3 right;
} camera;

uniform struct {
  vec4 position;
  vec4 powerDensity;
} lights[2];

precision highp sampler3D;
uniform struct{
  samplerCube env;
  sampler3D volume;
  sampler2D matcap1;
  sampler2D matcap2;
  float state;
  float layerWidth;
} scene;

in vec2 tex;
in vec4 rayDir;

out vec4 fragmentColor;


bool isShadowed(vec3 from, vec3 to, float maxH) {
  vec3 step = (to-from)/128.0;
  for(int i = 0; i<128; i++){
    if( (from.x<0.01)||(0.99<from.y) || (from.y<0.01) ||(0.99<from.y) || (from.z<0.01) || (0.99<from.z)){
      return true;
    }
    if(texture(scene.volume, from).r > maxH){
      return false;
    }
    from += step;
  }
  return true;
}

vec3 directLightning(vec3 viewDir, vec3 p, vec3 n, vec3 kd, vec3 ks, float shininess, float maxH){
  vec3 contrib = vec3(0,0,0);
  for(int i = 0; i < lights.length(); i++){
    vec3 lightDiff = lights[i].position.xyz - p * lights[i].position.w;
    vec3 lightDir = normalize(lightDiff);

    vec3 lightPowerDensity = lights[i].powerDensity.rgb / dot(lightDiff, lightDiff);

    if(scene.state != 2.0f || !isShadowed(p + n*0.1, lights[i].position.xyz, maxH)){
      vec3 h = normalize(lightDir + viewDir);
      contrib += lightPowerDensity * (
        kd * clamp(dot(lightDir, n), 0.0, 1.0)
        +
        ks * pow(clamp(dot(h, n), 0.0, 1.0), shininess)
      );
    }

  }
  return contrib;
}

vec3 correction(vec3 p, vec3 step, int n, float maxH){
  int timeOfHalf = 0;

  while(timeOfHalf <  n){
    float h = texture(scene.volume, p).r;

    step *= 0.5;
    timeOfHalf++;

    if(h > maxH){
      p -= step;
    } else {
      p += step;
    }
  }

  return p;
}

void main(void) {
  vec3 eye = camera.position;
  vec3 d = normalize(rayDir.xyz);

  fragmentColor = vec4(0, 0, 0, 1);

  float tstart = 1.0 / 0.0;
  float tend = 0.0;

  int[6] nf = int[](+1, -1, 0, 0, 0, 0);

  for(int i = 0; i<6; i++){
    vec3 n = vec3(nf[i], nf[(i+2)%6], nf[(i+4)%6]);
    vec3 o = vec3(0.5, 0.5, 0.5);

    float t = (0.5 - dot(eye - o, n)) / dot(n, d);
    vec3 p = eye + d * t - o;

    if(t < 0.0 || max(max(abs(p.x),abs(p.y)),abs(p.z)) > 0.501)
      continue;

    tstart = min(t, tstart);
    tend = max(t, tend);
  }

  vec3 p = eye + d * tstart;
  vec3 step = d * min((tend - tstart)/128.0, 0.05);

  float h = 0.0;

  if(scene.state == 3.0) {
    p= eye + d * tend;

    for (int i = 0; i<128; i++){
      float h_act = texture(scene.volume, p).r;
      h =  h + 1.0/128.0 * (h_act - h * h_act);

      p -= step;
    }

    if (h > 0.0){
      fragmentColor.rgb += h * vec3(1.0, 1.0, 1.0);
    } else {
      fragmentColor.rgb += vec3(0.0, 0.0, 0.0);//texture(scene.env, d.xyz).rgb;
    }
    return;
  }


  float sum_a = 0.0;
  float maxH = scene.layerWidth;
  int i = 0;

  for(int index = 0; index < 3 && sum_a < 1.0 && ((scene.state != 0.0 && scene.state != 2.0) || index == 0) && scene.state != 3.0; index++){
    float a = 0.0;

    for (; i<128; i++){
      h = texture(scene.volume, p).r;

      if (h > maxH) break;

      p += step;
    }

    p = correction(p, step, 3, maxH);

    a = min(1.0 - sum_a, maxH);

    if(scene.state == 0.0 || scene.state == 2.0){
      a = 1.0;
    } else {
      maxH += scene.layerWidth;
    }

    float eps = 0.01;

    if (h > 0.0){
      vec3 gradient =  vec3(
      texture(scene.volume, p + vec3(eps, 0, 0)).r - texture(scene.volume, p -  vec3(eps, 0, 0)).r,
      texture(scene.volume, p + vec3(0, eps, 0)).r - texture(scene.volume, p -  vec3(0, eps, 0)).r,
      texture(scene.volume, p + vec3(0, 0, eps)).r - texture(scene.volume, p -  vec3(0, 0, eps)).r
      );


      vec3 normal = normalize(gradient);
      a = min(a, 1.0 - dot(normal, -d));

      if (scene.state == 0.0f || scene.state == 2.0f){
        //vec3 kd = vec3(255.0/255.0*h, 158.0/255.0, 170.0/255.0);//vec3(0.1, 0.0, 0.05);//vec3(0.3, 0.3, 0.3);
        vec3 kd0 = vec3(237.0, 183.0, 144.0)/255.0;
        vec3 kd1 = vec3(225.0, 0.0, 0.0)/255.0;
        vec3 kd2 = vec3(255.0, 255.0, 255.0)/255.0;

        float ha = 0.2;
        float ht = ha*2.0*(1.0-h)*h + h*h;
        vec3 kd = kd0*1.0*(1.0-ht)*(1.0-ht) + kd1*2.0*(1.0-ht)*ht + kd2*1.0*ht*ht;

        vec3 ks = vec3(2.0, 2.0, 2.0)*0.5;//vec3(0.4, 0.2, 0.7);
        float shininess = 100.0;//15.0f;
        fragmentColor.rgb += a*directLightning(d, p, normal, kd, ks, shininess, maxH);
      } else if (scene.state == 1.0){

        if(index == 0) {
          fragmentColor.rgb += a*texture(
          scene.matcap2,
          vec2(
          dot(camera.right/*vec3(1.0, 0.0, 0.0)*/, normal) / 2.0f + 0.5f,
          dot(camera.up/*vec3(0.0, 1.0, 0.0)*/, normal) / 2.0f + 0.5f
          )
          ).rgb;
        } else {
          fragmentColor.rgb += a*texture(
          scene.matcap1,
          vec2(
          dot(camera.right/*vec3(1.0, 0.0, 0.0)*/, normal) / 2.0f + 0.5f,
          dot(camera.up/*vec3(0.0, 1.0, 0.0)*/, normal) / 2.0f + 0.5f
          )
          ).rgb;
        }

      }

    } else {
      fragmentColor.rgb += a*vec3(0.0, 0.0, 0.0);//texture(scene.env, d.xyz).rgb;
    }

    sum_a += a;
  }
}