plugins {//#plugins# apply plugins; already loaded in master project
  id("org.jetbrains.kotlin.js")
  kotlin("plugin.serialization")
}

dependencies {
  implementation(kotlin("stdlib-js"))//$ standard Kotlin classes 
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
}

val buildDir = File(project.parent?.projectDir ?: project.projectDir, "build")
if(!buildDir.exists())
  buildDir.mkdir()//$ create build folder
project.buildDir = File(buildDir, "client")
val webDir = File(buildDir, "web")
if(!webDir.exists())
  webDir.mkdir()//$ create build/web folder

kotlin {
  sourceSets.main {
    kotlin.srcDirs("src/main/kotlin", "../webglmath/src/main/kotlin", "../common/src/main/kotlin")
  }  
  js {
    browser {
      distribution {
        directory = webDir//$ transpiled code goes here
      }
    }
  }
}

val deployContent by tasks.registering(Copy::class) {
  from("src/main/content")//$ copy html and; media from here
  destinationDir = webDir
  include("**/*.*")    
}

val deployShaders by tasks.registering(Copy::class) {
  from("src/main/glsl")//$ copy glsl shaders; from here
  destinationDir = webDir
  include("**/*.*")    
}

val assemble by tasks.existing {
  dependsOn(deployContent)
  dependsOn(deployShaders)
}
