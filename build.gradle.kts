plugins {
    id("java")
    kotlin("jvm") version "2.1.21"

    id("com.google.devtools.ksp") version "2.1.21-2.0.2"
    id("eu.vendeli.telegram-bot") version "8.1.0"
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

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
}
