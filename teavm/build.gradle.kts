import org.gradle.plugins.ide.eclipse.model.EclipseModel

plugins {
  java
}

val appName = extra["appName"] as String
val gdxTeaVMVersion = project.property("gdxTeaVMVersion") as String
val mainClassName = "io.github.omegabird113.rollingforever.teavm.TeaVMBuilder"

sourceSets {
  main {
    resources.srcDir(rootProject.file("assets"))
  }
}

extensions.configure<EclipseModel>("eclipse") {
  project.name = "$appName-teavm"
}

// This must be at least 11, and no higher than the JDK version this project is built with.
java {
  targetCompatibility = JavaVersion.VERSION_21
  // This should probably be equal to targetCompatibility, above. This only affects the TeaVM module.
  sourceCompatibility = JavaVersion.VERSION_21
}

dependencies {
  implementation("com.github.xpenatan.gdx-teavm:backend-web:$gdxTeaVMVersion")
  implementation(project(":core"))
}

tasks.register<JavaExec>("runRelease") {
  description = "Run the TeaVM application hosted via a local Jetty server at http://localhost:8080/"
  group = "application"
  dependsOn(tasks.named("classes"))
  mainClass.set(mainClassName)
  classpath = sourceSets.main.get().runtimeClasspath
  args("run")
}

tasks.register<JavaExec>("runDebug") {
  description = "Run the TeaVM application with debug enabled hosted via a local Jetty server at http://localhost:8080/"
  group = "application"
  dependsOn(tasks.named("classes"))
  mainClass.set(mainClassName)
  classpath = sourceSets.main.get().runtimeClasspath
  args("debug", "run")
}

tasks.register<JavaExec>("buildRelease") {
  description = "Build the TeaVM application; doesn't run directly"
  group = "build"
  dependsOn(tasks.named("classes"))
  mainClass.set(mainClassName)
  classpath = sourceSets.main.get().runtimeClasspath
}

tasks.register<JavaExec>("buildDebug") {
  description = "Build the TeaVM application with debug enabled; doesn't run directly"
  group = "build"
  dependsOn(tasks.named("classes"))
  mainClass.set(mainClassName)
  classpath = sourceSets.main.get().runtimeClasspath
  args("debug")
}

// For backwards compatibility with the earlier run and build tasks.
tasks.register("run") {
  dependsOn("runRelease")
}
tasks.named("build") {
  dependsOn("buildRelease")
}
