# Framework monitoring examples
Welcome to the Framework monitoring example project. The goal of this project 
is to provide insights to using monitoring and alerting on various 
JVM frameworks.

### Requirements
- Gradle 7.4+
- OpenJDK 17+ 
- Grafana 
- Prometheus

Note: update your gradle wrapper!!!

### Frameworks
Particular framework implementation is located in the named folder. 
Most of the frameworks do contain the Java and Kotlin implementation. 
The following frameworks are currently considered:
- KTor, Koin
- Micronaut
- Spring-Boot
- Quarkus

# How to prepare
It is possible to execute the following commands in order to receive an 
experience with Grafana API. 

1. build the project from the root `$ gradlew clean build`
2. Building Framework Images: `$ buildMonitoringContainers.sh --help`
3. Run a docker-compose file: `$ docker-compose up`
4. Folder "monitoring" contains a Grafana dashboard files


# How to access Prometheus and Grafana
1. Promeheus `http:\\localhost:3500`
2. Grafana `http:\\localhost:3000`

Enjoy and Happy Observing!


![Frameworks-Monitoring-experiences](fw-ktor-monitoring-grafana.png)
