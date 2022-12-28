# Quarkus based test application

- [Dockerfile](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/main/quarkus/quarkus-reactive/Dockerfile) for JAR-based version.
- [Dockerfile](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/main/quarkus/quarkus-reactive/Dockerfile-native) for native image version.
- Dependency versions can be found in [gradle.properties](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/main/quarkus/quarkus-reactive/gradle.properties).

## How to start?

> Docker and psql is required to start locally database.

1. Create (or reuse if created - `docker start analysis`) Docker container with Database.

```shell
../../scripts/postgresContainer.sh
```

2. Start application

```shell
./gradlew quarkusDev
```



