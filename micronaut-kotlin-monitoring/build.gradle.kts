plugins {
    idea
    eclipse
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.jetbrains.kotlin.kapt") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.2.2"
}

version = "0.1"
group = "xyz.chrisime.monitoring.micronaut"

val kotlinVersion = project.properties["kotlinVersion"]
val micronautVersion = project.properties["micronautVersion"] as String

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut:micronaut-runtime")

    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.micrometer:micronaut-micrometer-registry-prometheus")

    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    runtimeOnly("ch.qos.logback:logback-classic")
}


application {
    mainClass.set("xyz.chrisime.monitoring.micronaut.ApplicationMainKt")
}

graalvmNative {
    toolchainDetection.set(false)
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

micronaut {
    version(micronautVersion)
    runtime("undertow")
    testRuntime("junit5")

    processing {
        incremental(true)
    }
}
