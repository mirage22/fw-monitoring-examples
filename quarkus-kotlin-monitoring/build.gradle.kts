import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

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

val quarkusGroupId: String by project
val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("${quarkusGroupId}:quarkus-micrometer-registry-prometheus")
    implementation("${quarkusGroupId}:quarkus-undertow")
    implementation("${quarkusGroupId}:quarkus-resteasy")
    implementation("${quarkusGroupId}:quarkus-micrometer")
    implementation("${quarkusGroupId}:quarkus-kotlin")

    testImplementation("${quarkusGroupId}:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
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
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xstrict-java-nullability-assertions")
            jvmTarget = JavaVersion.VERSION_17.toString()
            javaParameters = true
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

            events(TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.PASSED)
        }
    }
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.runtime.annotations.QuarkusMain")
}
