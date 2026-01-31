plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.dokka") version "2.1.0"
    id("com.vanniktech.maven.publish") version "0.36.0"
}

group = "org.capturecoop"
version = "1.0.7"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.capturecoop:defaultdepot:1.9.7")
}

kotlin {
    jvmToolchain(21)
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(dokkaHtml.outputDirectory)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("org.capturecoop", "colorcomposer", version.toString())

    pom {
        name.set("Color Composer")
        description.set("Color Utility Functions and Color Chooser")
        inceptionYear.set("2022")
        url.set("https://github.com/CaptureCoop/ColorComposer/")
        licenses {
            license {
                name.set("MIT")
                url.set("https://github.com/CaptureCoop/ColorComposer/blob/main/LICENSE")
                distribution.set("https://github.com/CaptureCoop/ColorComposer/blob/main/LICENSE")
            }
        }
        developers {
            developer {
                id.set("SvenWollinger")
                name.set("Sven Wollinger")
                url.set("https://wollinger.io")
            }
        }
        scm {
            url.set("https://github.com/CaptureCoop/ColorComposer/")
            connection.set("scm:git:git://github.com/CaptureCoop/ColorComposer.git")
            developerConnection.set("scm:git:ssh://git@github.com/CaptureCoop/ColorComposer.git")
        }
    }
}