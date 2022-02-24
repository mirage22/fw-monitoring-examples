#!/usr/bin/env bash
docker build -f ./DockerfileKTor --no-cache -t ktor-monitoring .
docker build -f ./DockerfileMicronaut --no-cache -t micronaut-monitoring .
docker build -f ./DockerfileSpringBoot --no-cache -t spring-boot-monitoring .
docker build -f ./DockerfileQuarkusJava --no-cache -t quarkus-java-monitoring .
