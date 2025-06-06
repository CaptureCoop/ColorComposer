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
    implementation("com.github.CaptureCoop:defaultdepot:5a221a7052")
}

kotlin {
    jvmToolchain(8)
}