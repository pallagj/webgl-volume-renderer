plugins {//#plugins# specify plugin versions here
  id("org.jetbrains.kotlin.js") version "1.5.30" apply false//#kotlin# handles Kotlin to JS transpiling #apply false# just load, apply in subprojects
  kotlin("plugin.serialization") version "1.5.30" apply false//#serialization# handles serialization
}

allprojects {//#allprojects# setting for all subprojects
  group = "vision.gears"
  version = "1.0"

  repositories {//#repositories# fetch dependencies from here
    mavenCentral()
    jcenter()
  }
}