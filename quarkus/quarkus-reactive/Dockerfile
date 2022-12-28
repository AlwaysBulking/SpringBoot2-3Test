#Build JAR
FROM latusikl/comparative-analysis:base-debian-jdk17-gradle7.4.1 as appBuild
WORKDIR /opt
COPY . .
RUN gradle build --no-daemon -x test

FROM openjdk:17-slim-bullseye
ARG JAR_DIR="/opt/build/quarkus-app/"
ARG USER="latusikl"
#Create system group called $USER then add new user that do not have password (cannot login),
#has empty GECOS field (name, surname etc.), has no home direcotry, has no login shell and is assigned to created group.
RUN    addgroup --system $USER && \
        adduser --disabled-password  \
                --system \
                --gecos "" \
                --no-create-home  \
                --shell /usr/sbin/nologin  \
                --ingroup $USER $USER
USER $USER

COPY --from=appBuild ${JAR_DIR} /opt/quarkus-app
#ENTRYPOINT ["java","-jar", "/opt/quarkus-app/quarkus-run.jar"]
ENTRYPOINT ["java","-Dcom.sun.management.jmxremote=true","-Dcom.sun.management.jmxremote.ssl=false","-Dcom.sun.management.jmxremote.authenticate=false","-Dcom.sun.management.jmxremote.port=9000","-Dcom.sun.management.jmxremote.rmi.port=9000","-Dcom.sun.management.jmxremote.host=127.0.0.1","-Djava.rmi.server.hostname=127.0.0.1","-jar", "/opt/quarkus-app/quarkus-run.jar"]
EXPOSE 8082
EXPOSE 9000
