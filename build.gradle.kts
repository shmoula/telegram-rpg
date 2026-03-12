plugins {
    id("java")
    application
    kotlin("jvm")

    id("com.gradleup.shadow")
    id("com.google.devtools.ksp")
    id("eu.vendeli.telegram-bot")
}

group = "cz.satorigeeks.telegramrpg"
version = "0.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

kotlin {
    jvmToolchain(23)
}

application {
    mainClass.set("cz.satorigeeks.telegramrpg.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
}

tasks.shadowJar {
    archiveBaseName.set("telegram-rpg")
    archiveClassifier.set("")
    archiveVersion.set("")

    manifest {
        attributes["Main-Class"] = "cz.satorigeeks.telegramrpg.MainKt"
    }
}