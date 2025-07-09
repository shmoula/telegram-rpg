plugins {
    id("java")
    kotlin("jvm") version "2.1.0"

    id("com.google.devtools.ksp") version "2.1.20-2.0.0"
    id("eu.vendeli.telegram-bot") version "8.1.0"
}

group = "cz.satorigeeks.telegramrpg"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

