import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val junit_jupiter_version: String by project
val java_version: String by project
val ktor_metrics_micrometer_version: String by project
val ktor_micrometer_prometheus_version: String by project

plugins {
    application
    kotlin("jvm") version  "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.wengnerits.monitoring.ktor"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.wengnerits.monitoring.ktor.ApplicationKt")
}


dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("org.junit.jupiter:junit-jupiter:$junit_jupiter_version")

    // Monitoring
    implementation("io.ktor:ktor-metrics-micrometer:$ktor_metrics_micrometer_version")
    implementation("io.insert-koin:koin-ktor:3.1.5")
    implementation("io.micrometer:micrometer-registry-prometheus:$ktor_micrometer_prometheus_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}
repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showCauses = true
        showStackTraces = true
        exceptionFormat = TestExceptionFormat.FULL

        events(TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.PASSED)
    }
}

tasks.compileKotlin{
    kotlinOptions.jvmTarget = java_version
}

tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = java_version
}
