buildscript {
  repositories {
    gradlePluginPortal()
  }
  dependencies {
    classpath("org.graalvm.buildtools.native:org.graalvm.buildtools.native.gradle.plugin:1.1.1")
  }
}

import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import org.graalvm.buildtools.gradle.NativeImagePlugin
import org.gradle.api.plugins.JavaApplication

apply<NativeImagePlugin>()

val appName = extra["appName"] as String

extensions.configure<GraalVMExtension>("graalvmNative") {
  binaries {
    named("main") {
      imageName.set(appName)
      mainClass.set(extensions.getByType<JavaApplication>().mainClass)
      buildArgs.add("-march=compatibility")
      jvmArgs.addAll("-Dfile.encoding=UTF8")
      sharedLibrary.set(false)
      resources.autodetect()
    }
  }
}

tasks.named<JavaExec>("run") {
  doNotTrackState("Running the app should not be affected by Graal.")
}

// Modified from https://lyze.dev/2021/04/29/libGDX-Internal-Assets-List/ ; thanks again, Lyze!
// This creates a resource-config.json file based on the contents of the assets folder (and the libGDX icons).
// This file is used by Graal Native to embed those specific files.
// This has to run before nativeCompile, so it runs at the start of an unrelated resource-handling command.
tasks.named("generateResourcesConfigFile") {
  doFirst {
    val assetsFolder = rootProject.file("assets")
    val resFolder = project.file("src/main/resources/META-INF/native-image/$appName")
    resFolder.mkdirs()

    val resFile = File(resFolder, "resource-config.json")
    resFile.delete()
    resFile.appendText(
      """{
  "resources":{
  "includes":[
    {
      "pattern": ".*("""
    )

    // This adds every filename in the assets/ folder to a pattern that adds those files as resources.
    fileTree(assetsFolder).forEach {
      // The backslash-Q and backslash-E escape the start and end of a literal string, respectively.
      resFile.appendText("\\\\Q${it.name}\\\\E|")
    }

    // We also match all of the window icon images this way and the font files that are part of libGDX.
    resFile.appendText(
      """libgdx.+\\.png|lsans.+)"
    }
  ]},
  "bundles":[]
}"""
    )
  }
}
