# Micronaut based test application

- [Dockerfile](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/main/micronaut/micronaut-reactive/Dockerfile) for JAR-based version.
- [Dockerfile](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/develop/micronaut/micronaut-reactive/Dockerfile-native) for native image version.
- Dependency versions can be found in [build.gradle](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/develop/micronaut/micronaut-reactive/build.gradle).

## How to start?

> Docker and psql is required to start locally database.

1. Create (or reuse if created - `docker start analysis`) Docker container with Database.

```shell
../../scripts/postgresContainer.sh
```

2. Start application

```shell
./gradlew run
```



