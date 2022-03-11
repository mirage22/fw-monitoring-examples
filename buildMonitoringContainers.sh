#!/usr/bin/env sh
set -e # exit when non-zero return

SCRIPT_NAME=$(basename "$0")
ARCH_TYPE="x86_64"
# detect the platform
if [[ "$(uname -m)" =~ "arm64" ]]; then
  ARCH_TYPE="arm64"
fi

function statusMessage() {
  echo "$1 image: '$2' , arch:'${ARCH_TYPE}', dockerFile:'$3'"
}

function buildDockerImageByProjectName(){
  if [[ -z "$1" && -z "$2" ]]; then
    displayHelp
    exit 1
  fi
  local dockerFileName="$1"
  local projectName="$2"
  statusMessage "building" $projectName $dockerFileName
  docker build -f "./${dockerFileName}" --platform "${ARCH_TYPE}" --no-cache -t "${projectName}" .
  statusMessage "building" $projectName $dockerFileName
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
            buildDockerImageByProjectName "DockerfileKTor" "ktor-monitoring"
            ;;
        --micronaut-java)
            buildDockerImageByProjectName "DockerfileMicronaut" "micronaut-monitoring"
            ;;
        --micronaut-kotlin)
            buildDockerImageByProjectName "DockerfileMicronautKt" "micronaut-kotlin-monitoring"
            ;;
        --spring-boot-java)
            buildDockerImageByProjectName "DockerfileSpringBoot" "spring-boot-monitoring"
            ;;
        --spring-boot-kotlin)
            buildDockerImageByProjectName "DockerfileSpringBootKt" "spring-boot-kotlin-monitoring"
            ;;
        --quarkus-java)
            buildDockerImageByProjectName "DockerfileQuarkusJava" "quarkus-monitoring"
            ;;
        --buildAll)
            buildDockerImageByProjectName "DockerfileKTor" "ktor-monitoring"
            buildDockerImageByProjectName "DockerfileMicronaut" "micronaut-monitoring"
            buildDockerImageByProjectName "DockerfileMicronautKt" "micronaut-kotlin-monitoring"
            buildDockerImageByProjectName "DockerfileSpringBoot" "spring-boot-monitoring"
            buildDockerImageByProjectName "DockerfileSpringBootKt" "spring-boot-kotlin-monitoring"
            buildDockerImageByProjectName "DockerfileQuarkusJava" "quarkus-java-monitoring"
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

if [[ -z "$1" ]]; then
  displayHelp
  exit 1
fi
parseArgs "$@"
