# Base Images

This directory contains definitions (Dockerfiles) for the base images that are used during multistage build of application images for the application building stage. 

## Images description

### Dockerfile-jar 
Used during the build of the application that is based on the JAR file. Deployed image can be found under the tag [base-debian-jdk17-gradle7.4.1](https://hub.docker.com/layers/latusikl/comparative-analysis/base-debian-jdk17-gradle7.4.1/images/sha256-1f6f7203f93f94b8729336968bd0da9f5f2bce01c8d2a98f48013d073e6f5c63?context=explore).

#### Included tools:

- Debian
- JDK 17
- Gradle 7.4.1

### Dockerfile-jar 
Used during the build of the application that is based on the native image file. Deployed image can be found under the tag [base-debian-jdk17-graal20.0.0.2-gradle7.4.1](https://hub.docker.com/layers/latusikl/comparative-analysis/base-debian-jdk17-graal20.0.0.2-gradle7.4.1/images/sha256-5af7319fba197f202e7e858da215127df9322f648f77ef1723f28982bcdf08a0?context=explore).

#### Included tools:

- Debian
- GraalVM 20.0.0.2
- Gradle 7.4.1
