import org.gradle.plugins.ide.eclipse.model.EclipseModel

val appName = extra["appName"] as String
val enableGraalNative = project.property("enableGraalNative") as String
val gdxVersion = project.property("gdxVersion") as String
val graalHelperVersion = project.property("graalHelperVersion") as String

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
}

extensions.configure<EclipseModel>("eclipse") {
  project.name = "$appName-core"
}

dependencies {
  api("com.badlogicgames.gdx:gdx:$gdxVersion")

  if (enableGraalNative == "true") {
    implementation("io.github.berstanio:gdx-svmhelper-annotations:$graalHelperVersion")
  }
}

tasks.named<Jar>("jar") {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
