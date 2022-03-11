#!/usr/bin/env sh
set -e # exit when non-zero return

SCRIPT_NAME=$(basename "$0")
ARCH_TYPE="x86_64"
# detect the platform
if [[ "$(uname -m)" =~ "arm64" ]]; then
  ARCH_TYPE="arm64"
fi

function statusMessage() {
  echo "$1 image: '$2' , arch:'${ARCH_TYPE}'"
}

function buildKtorImage(){
  local imageType="ktor-monitoring"
  statusMessage "building" $imageType
  docker build -f ./DockerfileKTor --platform "${ARCH_TYPE}" --no-cache --progress=plain -t ktor-monitoring .
  statusMessage "finished" $imageType
}


function buildMicronautJavaImage(){
  local imageType="micronaut-monitoring"
  statusMessage "building" $imageType
  docker build -f ./DockerfileMicronaut --platform "${ARCH_TYPE}" --no-cache --progress=plain -t micronaut-monitoring .
  statusMessage "finished" $imageType
}

function buildMicronautKotlinImage(){
  docker build -f ./DockerfileMicronautKt --platform "${ARCH_TYPE}" --no-cache -t micronaut-monitoring .
}

function buildSpringBootJavaImage(){
  local imageType="spring-boot-monitoring"
  statusMessage "building" $imageType
  docker build -f ./DockerfileSpringBoot --platform "${ARCH_TYPE}" --no-cache --progress=plain -t spring-boot-monitoring .
  statusMessage "finished" $imageType
}

function buildSpringBootKotlinImage(){
  docker build -f ./DockerfileSpringBootKt --platform "${ARCH_TYPE}" --no-cache -t spring-boot-kotlin-monitoring .
}

function buildQuarkusJavaImage(){
  local imageType="quarkus-monitoring"
  statusMessage "building" $imageType
  docker build -f ./DockerfileQuarkusJava --platform "${ARCH_TYPE}" --no-cache --progress=plain -t quarkus-java-monitoring .
  statusMessage "finished" $imageType
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
    printf " \t%s\t%s\n" "--micronaut-kotlin" "build Micronaut-Kotlin docker image"
    printf " \t%s\t%s\n" "--spring-boot-java" "build Spring-Boot-Java docker image"
    printf " \t%s\t%s\n" "--spring-boot-kotlin" "build Spring-Boot-Kotlin docker image"
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
        --micronaut-kotlin)
            buildMicronautKotlinImage
            ;;
        --spring-boot-java)
            buildSpringBootJavaImage
            ;;
        --spring-boot-kotlin)
            buildSpringBootKotlinImage
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
