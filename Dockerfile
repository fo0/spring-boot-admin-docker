# run stage
FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="fo0@fo0me"

RUN apk add --no-cache curl
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /opt/spring-boot-admin-docker/app.jar
WORKDIR /opt/spring-boot-admin-docker
EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","app.jar"]
