plugins {
    idea
    eclipse

    kotlin("jvm") version "1.6.10"
    kotlin("plugin.allopen") version "1.6.10"

    id("io.quarkus") version "2.7.3.Final"
}

repositories {
    mavenCentral()
}

val quarkusVersion: String by project

dependencies {
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${quarkusVersion}"))
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
}

group = "com.wengnerits.monitoring.quarkus"
version = "1.0.0-SNAPSHOT"

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = sourceCompatibility
            apiVersion = "1.6"
            languageVersion = "1.6"
            jvmTarget = JavaVersion.VERSION_17.toString()
            javaParameters = true
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
