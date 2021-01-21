FROM maven:3.6-jdk-11 AS build

#build


COPY src /usr/src/providerServer/src
COPY pom.xml /usr/src/providerServer/pom.xml
RUN mvn -f /usr/src/providerServer/pom.xml clean package


# application container

FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/providerServer/target /usr/app/

EXPOSE 9091/tcp
EXPOSE 9092/tcp

COPY providerServer.sh /run.sh

ENTRYPOINT [ "sh", "/run.sh" ]