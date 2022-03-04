#!/usr/bin/env sh
set -e # exit when non-zero return

SCRIPT_NAME=$(basename "$0")
ARCH_TYPE="x86_64"
# detect the platform
if [[ "`uname -m`" =~ "arm64" ]]; then
  ARCH_TYPE="arm64"
fi

function buildKtorImage(){
 docker build -f ./DockerfileKTor --platform "${ARCH_TYPE}" --no-cache -t ktor-monitoring .
}

function buildMicronautJavaImage(){
  docker build -f ./DockerfileMicronaut --platform "${ARCH_TYPE}" --no-cache -t micronaut-monitoring .
}

function buildSpringBootJavaImage(){
  docker build -f ./DockerfileSpringBoot --platform "${ARCH_TYPE}" --no-cache -t spring-boot-monitoring .
}

function buildQuarkusJavaImage(){
  docker build -f ./DockerfileQuarkusJava --platform "${ARCH_TYPE}" --no-cache -t quarkus-java-monitoring .
}

function error_print() {
    echo "$@" >&2
}

function displayHelp(){
  echo "usage: $ ./${SCRIPT_NAME} with options:"
  {
    printf " \t%s\t%s\n" "--help" "show the help dialog"
    printf " \t%s\t%s\n" "--ktor" "build KTor docker image"
    printf " \t%s\t%s\n" "--micronaut-java" "build Micronaut-Java docker image"
    printf " \t%s\t%s\n" "--spring-boot-java" "build Spring-Boot-Java docker image"
    printf " \t%s\t%s\n" "--quarkus-java" "build Quarkus-Java docker image"
    printf " \t%s\t%s\n" "--buildAll" "build all available images"

  } | column -ts $'\t'
}

function parseArgs(){
  while [ $# -gt 0 ]; do
      case "$1" in
        --help)
            displayHelp
            exit 0
            ;;
        --ktor)
            buildKtorImage
            ;;
        --micronaut-java)
            buildMicronautJavaImage
            ;;
        --spring-boot-java)
            buildSpringBootJavaImage
            ;;
        --quarkus-java)
            buildQuarkusJavaImage
            ;;
        --buildAll)
            buildKtorImage
            buildMicronautJavaImage
            buildSpringBootJavaImage
            buildQuarkusJavaImage
            ;;
        *)
            error_print "unknown arguments: $@"
            displayHelp
            exit 1
            ;;
      esac
      shift
  done
}

parseArgs "$@"
