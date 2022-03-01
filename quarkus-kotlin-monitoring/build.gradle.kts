import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    eclipse

    kotlin("jvm") version "1.6.10"
    kotlin("plugin.allopen") version "1.6.10"

    id("io.quarkus") version "2.7.2.Final"
}

repositories {
    mavenCentral()
}

val quarkusPluginId="io.quarkus"
val quarkusPlatformGroupId="io.quarkus.platform"
val quarkusPlatformArtifactId="quarkus-bom"
val quarkusPlatformVersion="2.7.2.Final"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("${quarkusPluginId}:quarkus-micrometer-registry-prometheus")
    implementation("${quarkusPluginId}:quarkus-undertow")
    implementation("${quarkusPluginId}:quarkus-resteasy")
    implementation("${quarkusPluginId}:quarkus-micrometer")
    implementation("${quarkusPluginId}:quarkus-kotlin")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "com.wengnerits.monitoring.quarkus"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_17.toString()
        javaParameters = true
    }
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.runtime.annotations.QuarkusMain")
}
