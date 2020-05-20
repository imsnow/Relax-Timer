buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(Deps.androidGradlePlugin)
        classpath(Deps.cocoapodsext)
        classpath(kotlin("gradle-plugin", Versions.kotlin))
    }
}

val kotlinVersion = KotlinVersion

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/ekito/koin")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}