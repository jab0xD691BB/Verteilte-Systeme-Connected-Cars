FROM maven:3.6-jdk-11 AS build

#build


COPY src /usr/src/thrift/src
COPY pom.xml /usr/src/thrift/pom.xml
RUN mvn -f /usr/src/thrift/pom.xml clean package


# application container

FROM openjdk:8-jre-alpine
COPY --from=build /usr/src/thrift/target /usr/app/

EXPOSE 9090/tcp
EXPOSE 9080/tcp

COPY thrift.sh /run.sh

ENTRYPOINT [ "sh", "/run.sh" ]