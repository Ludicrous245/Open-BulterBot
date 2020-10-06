import org.gradle.internal.jvm.Jvm
import org.apache.tools.ant.filters.ReplaceTokens

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:5.2.0")
    }
}

apply(plugin = "com.github.johnrengelman.shadow")

plugins {
    application
    id("com.github.johnrengelman.shadow") version "5.2.0"
    id("idea")
    kotlin("jvm") version "1.3.72"
}

group = "com.Ludicrous245"
version = "1.3.5-LTS"

repositories {
    mavenCentral()
    jcenter()
    maven(url="https://dl.bintray.com/sedmelluq/com.sedmelluq")
    maven( url= "https://jitpack.io")
    maven( url= "https://dl.bintray.com/natanbc/maven")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compile("net.dv8tion:JDA:4.2.0_168")
    implementation("com.sedmelluq:lavaplayer-natives-extra:1.3.13")
    implementation("com.sedmelluq:lavaplayer:1.3.50")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.0")
    compile("com.google.apis:google-api-services-youtube:v3-rev212-1.25.0")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

tasks.withType<Jar>() {

}

application{
    mainClassName = "com.Ludicrous245.BootloaderKt"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    jar{
        finalizedBy(shadowJar)
        manifest{
            attributes["Main-Class"] = application.mainClassName
        }
        from(configurations.runtime.get().map {if (it.isDirectory) it else zipTree(it)})
    }
}
