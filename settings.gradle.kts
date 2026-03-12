pluginManagement {
    plugins {
        kotlin("jvm") version "2.1.21"
        id("com.google.devtools.ksp") version "2.1.21-2.0.2"
        id("eu.vendeli.telegram-bot") version "8.1.0"
        id("com.gradleup.shadow") version "9.3.2"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "telegram-rpg"