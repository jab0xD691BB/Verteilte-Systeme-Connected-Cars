FROM maven:3.6-jdk-11 AS build

#build

COPY src /usr/src/station/src
COPY pom.xml /usr/src/station/pom.xml
RUN mvn -f /usr/src/station/pom.xml clean package


# application container

FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/station/target /usr/app

#ordner mit den werten in container-root kopieren & favico
COPY values values
COPY favicon.ico favicon.ico
COPY index.html index.html
COPY index.css index.css
COPY index.js index.js

# default paramters 
ARG sensorname="Default"
ARG sensorip="127.0.0.1"
ARG sensorport="9876"


EXPOSE 9876/udp
EXPOSE 9877/udp
EXPOSE 9878/udp
EXPOSE 9879/udp
EXPOSE 3124/tcp
EXPOSE 3125/tcp

COPY station.sh /run.sh

ENTRYPOINT [ "sh", "/run.sh" ]