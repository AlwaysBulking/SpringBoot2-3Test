# Workflows

This directory contains GitHub actions workflows used to build applications and publish images to Docker registry.

## Build workflows

All of the projects that are build uses Gradle as build tool. Project specific workflows are based on the generic [build-test-gradle.yml](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/main/.github/workflows/build-test-gradle.yml) workflow.

## Publishing images

Publishing of Docker images containing test application as well as base images used for build purpose are based on the common [publish-image.yml](https://github.com/latusikl/jvm-frameworks-comparative-assessment/blob/main/.github/workflows/publish-image.yml) workflow.