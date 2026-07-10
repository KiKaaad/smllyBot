plugins {
    java
    kotlin("jvm") version "2.4.0"
    id("com.gradleup.shadow") version "9.4.1"
    id("org.sonarqube") version "7.3.1.8318"
}

group = "kikaaad.smlly"
version = "0.4.7-beta"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.kika.smllybot.Main"
    }
}

dependencies {
    implementation("com.zaxxer:HikariCP:7.1.0")
    implementation("org.postgresql:postgresql:42.7.10")
    implementation("ch.qos.logback:logback-classic:1.5.32")
    implementation("ch.qos.logback:logback-core:1.5.32")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("net.dv8tion:JDA:6.5.0")
    implementation(libs.io.github.cdimascio.dotenv.java)
    implementation(libs.org.jetbrains.kotlin.kotlin.stdlib.jdk8)
    testImplementation(libs.org.jetbrains.kotlin.kotlin.test)
}

sonar {
    properties {
        property("sonar.projectKey", "KiKaaad_smllyBotDiscordJDA")
        property("sonar.organization", "kikaaad")
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("--enable-preview")
}




