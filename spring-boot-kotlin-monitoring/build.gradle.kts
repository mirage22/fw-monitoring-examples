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

val kotlinCompatibilityVersion = "16"

repositories {
    mavenCentral()
}

tasks {
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
            jvmTarget = kotlinCompatibilityVersion
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = kotlinCompatibilityVersion
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }

    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
