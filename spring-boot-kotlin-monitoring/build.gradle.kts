plugins {
    idea
    eclipse
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

group = "com.wengnerits.monitoring.spring.boot.kt"
version = "0.0.1-SNAPSHOT"

val micrometerVersion:String by project
val usedJavaVersion = JavaVersion.VERSION_17
val kotlinCompatibilityVersion = "16"

repositories {
    mavenCentral()
}

tasks {
    java {
        sourceCompatibility = usedJavaVersion
        targetCompatibility = usedJavaVersion
    }

    jar {
        enabled = true
    }

    bootJar {
        manifest {
            attributes to "Start-Class" to "xyz.chrisime.monitoring.spring.boot.ApplicationMainKt"
        }
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = usedJavaVersion.toString()
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("io.micrometer:micrometer-core:${micrometerVersion}")
    implementation("io.micrometer:micrometer-registry-prometheus:${micrometerVersion}")
}
