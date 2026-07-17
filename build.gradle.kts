plugins {
    java
    kotlin("jvm") version "2.4.0"
    id("com.gradleup.shadow") version "9.5.1"
}

group = "kikaaad.smlly"
version = "0.4.16-beta"

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
    implementation("org.postgresql:postgresql:42.7.11")
    implementation("ch.qos.logback:logback-classic:1.5.38")
    implementation("ch.qos.logback:logback-core:1.5.38")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("net.dv8tion:JDA:6.5.0")
    implementation("club.minnced:jda-ktx:0.15.0")
    implementation(libs.io.github.cdimascio.dotenv.java)
    implementation(libs.org.jetbrains.kotlin.kotlin.stdlib.jdk8)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    testImplementation(libs.org.jetbrains.kotlin.kotlin.test)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("--enable-preview")
}




