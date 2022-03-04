import io.micronaut.gradle.MicronautRuntime
import io.micronaut.gradle.MicronautTestRuntime
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    idea
    eclipse

    kotlin("jvm") version "1.6.10"
    kotlin("kapt") version "1.6.10"

    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.2.2"
}

version = "1.0-SNAPSHOT"
group = "xyz.chrisime.monitoring.micronaut"

val kotlinCompatibilityVersion: String by project
val kotlinBaseVersion: String by project
val micronautVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("io.micronaut:micronaut-bom:${micronautVersion}"))
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut:micronaut-runtime")

    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")

    runtimeOnly("ch.qos.logback:logback-classic")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = kotlinCompatibilityVersion
            apiVersion = kotlinBaseVersion
            languageVersion = kotlinBaseVersion
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = kotlinCompatibilityVersion
            apiVersion = kotlinBaseVersion
            languageVersion = kotlinBaseVersion
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    test {
        useJUnitPlatform()

        testLogging {
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
            exceptionFormat = TestExceptionFormat.FULL
            displayGranularity = 2

            events(TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.PASSED)
        }
    }

    graalvmNative {
        toolchainDetection.set(false)
    }

    micronaut {
        application.mainClass.set("xyz.chrisime.monitoring.micronaut.ApplicationMainKt")

        enableNativeImage(false)
                .version(micronautVersion)
                .runtime(MicronautRuntime.UNDERTOW)
                .testRuntime(MicronautTestRuntime.JUNIT_5)

        processing {
            incremental(true)
            annotations("xyz.chrisime")
        }
    }
}
