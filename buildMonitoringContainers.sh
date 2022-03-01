#!/usr/bin/env bash
ARCH=$(uname -m)
if [ "$ARCH" = 'arm64' ]; then
  DOCKER_ARCH='linux/arm64/v8'
else
  DOCKER_ARCH='linux/amd64'
fi

docker build -f ./DockerfileKTor --platform $DOCKER_ARCH --no-cache -t ktor-monitoring .
docker build -f ./DockerfileMicronaut --platform $DOCKER_ARCH --no-cache -t micronaut-monitoring .
docker build -f ./DockerfileMicronautKt --platform $DOCKER_ARCH --no-cache -t micronaut-monitoring-kt .
docker build -f ./DockerfileSpringBoot --platform $DOCKER_ARCH --no-cache -t spring-boot-monitoring .
docker build -f ./DockerfileQuarkusJava --platform $DOCKER_ARCH --no-cache -t quarkus-java-monitoring .
