FROM maven:3.6-jdk-11 AS build

#build


COPY src /usr/src/sensor/src
COPY pom.xml /usr/src/sensor/pom.xml
RUN mvn -f /usr/src/sensor/pom.xml clean package


# application container

FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/sensor/target /usr/app/

COPY sensor.sh /run.sh

ENTRYPOINT [ "sh", "/run.sh" ]