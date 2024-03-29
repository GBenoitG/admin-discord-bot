import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
    application
}

group = "com.bendev.discordbot"
version = "1.0-SNAPSHOT"

val mainClassName = "com.bendev.discordbot.BotKt"

repositories {
    mavenCentral()
    maven {
        name = "m2-dv8tion"
        url = URI.create("https://m2.dv8tion.net/releases")
    }
}

dependencies {

//    Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

//    JDA (Java Discord API)
    implementation("net.dv8tion:JDA:4.4.0_352") {
        exclude(module = "opus-java")
    }

//    Test
    testImplementation(group= "junit", name= "junit", version= "4.13.2")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = mainClassName
    }

    // To add all the dependencies otherwise a "NoClassDefFoundError" error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}