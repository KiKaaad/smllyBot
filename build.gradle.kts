plugins {
    java
    kotlin("jvm") version "2.1.10"
    id("com.gradleup.shadow") version "9.4.1"
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
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("org.postgresql:postgresql:42.7.10")
    implementation("ch.qos.logback:logback-classic:1.5.32")
    implementation("ch.qos.logback:logback-core:1.5.32")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("net.dv8tion:JDA:6.5.0")
    implementation(libs.io.github.cdimascio.dotenv.java)
    implementation(libs.org.jetbrains.kotlin.kotlin.stdlib.jdk8)
    testImplementation(libs.org.jetbrains.kotlin.kotlin.test)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("--enable-preview")
}




