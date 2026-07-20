/*
 RollingForever - The Java Roll-a-ball game
 Copyright (c) 2026 Omegabird113.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

import io.github.fourlastor.construo.ConstruoPluginExtension
import io.github.fourlastor.construo.Target
import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import org.gradle.jvm.application.tasks.CreateStartScripts
import org.gradle.plugins.ide.eclipse.model.EclipseModel
import java.util.Locale

plugins {
  application
  id("io.github.fourlastor.construo") version "2.2.0"
  id("org.graalvm.buildtools.native") version "1.1.5" apply false
}

val appName = extra["appName"] as String
val enableGraalNative = project.property("enableGraalNative") as String
val gdxVersion = project.property("gdxVersion") as String
val graalHelperVersion = project.property("graalHelperVersion") as String
val lwjgl3Version = project.property("lwjgl3Version") as String
val projectVersion = project.property("projectVersion") as String
val os = System.getProperty("os.name").lowercase(Locale.ROOT)
val runtimeClasspath = configurations.named("runtimeClasspath")
val jarTask = tasks.named<Jar>("jar")
val commonJarExcludes = listOf(
  "META-INF/INDEX.LIST",
  "META-INF/*.SF",
  "META-INF/*.DSA",
  "META-INF/*.RSA",
  "META-INF/maven/**",
)

fun Jar.configureRunnableJar(fileName: String) {
  archiveFileName.set(fileName)
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  dependsOn(runtimeClasspath)
  from({
    runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
  })
  exclude(commonJarExcludes)
  manifest {
    attributes(
      "Main-Class" to application.mainClass.get(),
      "Enable-Native-Access" to "ALL-UNNAMED",
      "Multi-Release" to "true",
    )
  }
  doLast {
    archiveFile.get().asFile.setExecutable(true, false)
  }
}

sourceSets {
  main {
    resources.srcDir(rootProject.file("assets"))
  }
}

application {
  mainClass.set("io.github.omegabird113.rollingforever.lwjgl3.Lwjgl3Launcher")
  applicationName = appName
}

extensions.configure<EclipseModel>("eclipse") {
  project.name = "$appName-lwjgl3"
}

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

if (JavaVersion.current().isJava9Compatible) {
  tasks.named<JavaCompile>("compileJava") {
    options.release.set(21)
  }
}

dependencies {
  implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
  implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
  implementation("com.badlogicgames.gdx:gdx-lwjgl3-angle:$gdxVersion")
  implementation(project(":core"))

  if (enableGraalNative == "true") {
    implementation("io.github.berstanio:gdx-svmhelper-backend-lwjgl3:$graalHelperVersion")
  }

  // Forces LWJGL3 to use at least $lwjgl3Version, currently 3.4.1, to avoid problems on Java 25 and up.
  constraints {
    implementation("org.lwjgl:lwjgl:$lwjgl3Version")
    implementation("org.lwjgl:lwjgl-glfw:$lwjgl3Version")
    implementation("org.lwjgl:lwjgl-jemalloc:$lwjgl3Version")
    implementation("org.lwjgl:lwjgl-openal:$lwjgl3Version")
    implementation("org.lwjgl:lwjgl-opengl:$lwjgl3Version")
    implementation("org.lwjgl:lwjgl-stb:$lwjgl3Version")
  }
}

tasks.named<JavaExec>("run") {
  workingDir = rootProject.file("assets")
  // You can uncomment the next line if your IDE claims a build failure even when the app closed properly.
  // isIgnoreExitValue = true
  jvmArgs("--enable-native-access=ALL-UNNAMED")
  if (os.contains("mac")) {
    jvmArgs("-XstartOnFirstThread")
  }
}

jarTask.configure {
  // Setting the manifest makes the JAR runnable.
  // Enabling native access helps avoid a warning when Java 24 or later runs the JAR.
  // Setting Multi-Release to true allows LWJGL3 to use different classes on recent Java versions.
  configureRunnableJar("$appName-$projectVersion.jar")
}

// Builds a JAR that only includes the files needed to run on macOS, not Windows or Linux.
// The file size for a Mac-only JAR is about 7MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarMac") {
  group = "build"
  from(sourceSets.main.get().output)
  configureRunnableJar("$appName-$projectVersion-mac.jar")
  exclude("windows/x86/**", "windows/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**", "**/*.dll", "**/*.so")
}

// Builds a JAR that only includes the files needed to run on Linux, not Windows or macOS.
// The file size for a Linux-only JAR is about 5MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarLinux") {
  group = "build"
  from(sourceSets.main.get().output)
  configureRunnableJar("$appName-$projectVersion-linux.jar")
  exclude("windows/x86/**", "windows/x64/**", "macos/arm64/**", "macos/x64/**", "**/*.dll", "**/*.dylib")
}

// Builds a JAR that only includes the files needed to run on Windows, not Linux or macOS.
// The file size for a Windows-only JAR is about 6MB smaller than a cross-platform JAR.
tasks.register<Jar>("jarWin") {
  group = "build"
  from(sourceSets.main.get().output)
  configureRunnableJar("$appName-$projectVersion-win.jar")
  exclude("macos/arm64/**", "macos/x64/**", "linux/arm32/**", "linux/arm64/**", "linux/x64/**", "**/*.dylib", "**/*.so")
}

extensions.configure<ConstruoPluginExtension>("construo") {
  // Name of the executable.
  name.set(appName)
  // Human-readable name, used for example in the `.app` name for macOS.
  humanName.set(appName)
  jlink {
    guessModulesFromJar.set(false)
    // You may need to add more modules, as needed.
    modules.addAll("java.base", "java.management", "java.desktop", "jdk.unsupported")
  }

  targets.register("linuxX64", Target.Linux::class.java) {
    architecture.set(Target.Architecture.X86_64)
    jdkUrl.set("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.11%2B10/OpenJDK21U-jdk_x64_linux_hotspot_21.0.11_10.tar.gz")
    // Linux does not currently have a way to set the icon on the executable.
  }
  targets.register("macM1", Target.MacOs::class.java) {
    architecture.set(Target.Architecture.AARCH64)
    jdkUrl.set("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.11%2B10/OpenJDK21U-jdk_aarch64_mac_hotspot_21.0.11_10.tar.gz")
    // macOS needs an identifier.
    identifier.set("io.github.omegabird113.rollingforever.$appName")
    // Optional: icon for macOS, as an ICNS file.
    macIcon.set(project.file("icons/logo.icns"))
  }
  targets.register("macX64", Target.MacOs::class.java) {
    architecture.set(Target.Architecture.X86_64)
    jdkUrl.set("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.11%2B10/OpenJDK21U-jdk_x64_mac_hotspot_21.0.11_10.tar.gz")
    // macOS needs an identifier.
    identifier.set("io.github.omegabird113.rollingforever.$appName")
    // Optional: icon for macOS, as an ICNS file.
    macIcon.set(project.file("icons/logo.icns"))
  }
  targets.register("winX64", Target.Windows::class.java) {
    architecture.set(Target.Architecture.X86_64)
    // Optional: icon for Windows, as a PNG.
    icon.set(project.file("icons/logo.png"))
    jdkUrl.set("https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.11%2B10/OpenJDK21U-jdk_x64_windows_hotspot_21.0.11_10.zip")
    // Uncomment the next line to show a console when the game runs, to print messages.
    // useConsole.set(true)
  }
}

// Equivalent to the jar task; here for compatibility with gdx-setup.
tasks.register("dist") {
  dependsOn("jar")
}

distributions {
  main {
    contents {
      into("libs") {
        val jarFileName = jarTask.get().archiveFile.get().asFile.name
        runtimeClasspath.get().files
          .filter { it.name != jarFileName }
          .forEach { exclude(it.name) }
      }
    }
  }
}

tasks.named<CreateStartScripts>("startScripts") {
  dependsOn(":lwjgl3:jar")
  classpath = files(jarTask.flatMap { it.archiveFile })
}

// Helps if debugging on Linux with an Nvidia GPU.
// This means StartupHelper won't try to restart the JVM, which can prevent debugging.
// This only applies to Gradle tasks, not main methods debugged when launching a main() method directly.
// As a more general solution, set the environment variable __GL_THREADED_OPTIMIZATIONS to 0 globally, on Linux
// machines with Nvidia GPUs where you need to debug LWJGL3 apps and games.
// You can also set __GL_THREADED_OPTIMIZATIONS to 0 in run configurations, which you would need per main() method.
// StartupHelper will still restart the JVM to set this environment variable when run as a distributable JAR, which is
// a good thing for end users. They won't need to ever set the debug-specific environment variable.
tasks.withType<JavaExec>().configureEach {
  environment("__GL_THREADED_OPTIMIZATIONS", 0)
}

if (enableGraalNative == "true") {
  apply(plugin = "org.graalvm.buildtools.native")

  extensions.configure<GraalVMExtension>("graalvmNative") {
    binaries {
      named("main") {
        imageName.set(appName)
        mainClass.set(application.mainClass)
        buildArgs.add("-march=compatibility")
        jvmArgs.add("-Dfile.encoding=UTF8")
        sharedLibrary.set(false)
        resources.autodetect()
      }
    }
  }

  tasks.named<JavaExec>("run") {
    doNotTrackState("Running the app should not be affected by Graal.")
  }

  // This creates a resource-config.json file based on the assets folder, libGDX icons, and libGDX font files.
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

      fileTree(assetsFolder).forEach {
        resFile.appendText("\\\\Q${it.name}\\\\E|")
      }

      resFile.appendText(
        """libgdx.+\\.png|lsans.+)"
    }
  ]},
  "bundles":[]
}"""
      )
    }
  }
}
