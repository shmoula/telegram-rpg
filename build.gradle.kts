plugins {
    id("java")
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
    alias(libs.plugins.ksp)
    alias(libs.plugins.telegram.bot)
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
    implementation(libs.dotenv.kotlin)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.sqlite.jdbc)
    implementation(libs.kotlinx.serialization.json)
}

tasks.shadowJar {
    archiveBaseName.set("telegram-rpg")
    archiveClassifier.set("")
    archiveVersion.set("")

    manifest {
        attributes["Main-Class"] = "cz.satorigeeks.telegramrpg.MainKt"
    }
}
