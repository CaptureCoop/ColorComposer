plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.capturecoop"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.CaptureCoop:defaultdepot:3d64cdaf6b")
}

kotlin {
    jvmToolchain(8)
}