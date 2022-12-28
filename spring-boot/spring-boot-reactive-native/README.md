# Spring Boot based test application for JAR

- [Dockerfile](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/main/spring-boot/spring-boot-reactive-native/Dockerfile-native) for JAR-based version.
- Dependency versions can be found in [build.gradle](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/main/spring-boot/spring-boot-reactive-native/build.gradle).

## How to start?

> Docker and psql is required to start locally database.

1. Create (or reuse if created - `docker start analysis`) Docker container with Database.

```shell
../../scripts/postgresContainer.sh
```

2. Start application

```shell
./gradlew bootRun
```
