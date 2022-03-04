import io.micronaut.gradle.MicronautRuntime
import io.micronaut.gradle.MicronautTestRuntime
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    idea
    eclipse
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.jetbrains.kotlin.kapt") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.2.2"
}

version = "1.0-SNAPSHOT"
group = "xyz.chrisime.monitoring.micronaut"

val kotlinCompatibilityVersion: String by project
val micronautVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut:micronaut-runtime")

    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")

    runtimeOnly("ch.qos.logback:logback-classic")
}

tasks {
    application {
        mainClass.set("xyz.chrisime.monitoring.micronaut.ApplicationMainKt")
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

    micronaut {
        enableNativeImage(false)
                .version(micronautVersion)
                .runtime(MicronautRuntime.UNDERTOW)
                .testRuntime(MicronautTestRuntime.JUNIT_5)

        processing {
            incremental(true)
        }
    }

    graalvmNative {
        toolchainDetection.set(false)
    }
}
