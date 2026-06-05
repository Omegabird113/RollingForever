/*
 RollingForever - The Java Roll-a-ball game
 Copyright (c) 2026 Omegabird113.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.gradle.plugins.ide.eclipse.model.EclipseModel
import org.gradle.plugins.ide.idea.model.IdeaModel

buildscript {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
    google()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
  }
}

allprojects {
  apply(plugin = "eclipse")
  apply(plugin = "idea")

  // This allows you to "Build and run using IntelliJ IDEA", an option in IDEA's Settings.
  extensions.configure<IdeaModel>("idea") {
    module {
      outputDir = file("build/classes/java/main")
      testOutputDir = file("build/classes/java/test")
    }
  }
}

configure(subprojects) {
  apply(plugin = "java-library")

  extensions.configure<JavaPluginExtension>("java") {
    sourceCompatibility = JavaVersion.VERSION_21
  }

  // From https://lyze.dev/2021/04/29/libGDX-Internal-Assets-List/
  // The article can be helpful when using assets.txt in your project.
  tasks.register("generateAssetList") {
    val assetsFolder = rootProject.file("assets")
    val assetsFile = File(assetsFolder, "assets.txt")

    inputs.dir(assetsFolder)

    doLast {
      assetsFile.delete()
      fileTree(assetsFolder)
        .map { it.relativeTo(assetsFolder).path }
        .sorted()
        .forEach { assetsFile.appendText("$it\n") }
    }
  }

  tasks.named<ProcessResources>("processResources") {
    dependsOn("generateAssetList")
  }

  tasks.withType<JavaCompile>().configureEach {
    options.isIncremental = true
  }
}

subprojects {
  version = project.property("projectVersion") as String
  extra["appName"] = "Rolling Forever"

  repositories {
    mavenCentral()
    // You may want to remove the following line if you have errors downloading dependencies.
    mavenLocal()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
    maven("https://jitpack.io")
    maven("https://teavm.org/maven/repository/")
  }
}

extensions.configure<EclipseModel>("eclipse") {
  project.name = "Rolling Forever-parent"
}
