# build stage
FROM maven:3-openjdk-11 AS build-stage
LABEL maintainer="fo0@fo0me"

COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean package

# run stage
FROM adoptopenjdk/openjdk11:alpine-slim
LABEL maintainer="fo0@fo0me"

ARG BUILD_DATE
ARG BUILD_NAME
ARG BUILD_VERSION
ARG VCS_REF

LABEL org.label-schema.schema-version="1.0"
LABEL org.label-schema.build-date=$BUILD_DATE
LABEL org.label-schema.name=$BUILD_NAME
LABEL org.label-schema.description="Spring Boot Admin Docker"
LABEL org.label-schema.url="https://github.com/fo0/spring-boot-admin-docker"
LABEL org.label-schema.vcs-url="https://github.com/fo0/spring-boot-admin-docker"
LABEL org.label-schema.vcs-ref=$VCS_REF
LABEL org.label-schema.vendor="fo0"
LABEL org.label-schema.version=$BUILD_VERSION
LABEL org.label-schema.docker.cmd="docker run -d -p 1111:1111 --name spring-boot-admin fo0me/spring-boot-admin"

RUN apk add --no-cache curl
COPY --from=build-stage /tmp/target/*.jar /opt/spring-boot-admin-docker/app.jar
WORKDIR /opt/spring-boot-admin-docker
EXPOSE 1111

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","app.jar"]