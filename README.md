Spring Boot Admin
========================

Info
----
- Name: `fo0me/spring-boot-admin`
- Version: `latest`
- Fork of `slydeveloper/spring-boot-admin`
- [Docker Hub](https://hub.docker.com/r/fo0me/spring-boot-admin/)

Details
--------
- Image based on `adoptopenjdk/openjdk17:alpine-slim`
- Spring Boot Admin version: check pom
- Spring Boot version: check pom
- Default port: `8080`
- Default user: `admin`
- Default password: `secret`
- URL: `http://localhost:8080`
- Health check URL - `http://localhost:8080/health`

Usage
--------
`docker run -d -p 8080:8080 --name spring-boot-admin fo0me/spring-boot-admin:latest`

Configuration via environment variables 
---------------------------------------
* `SPRING_BOOT_ADMIN_USER_NAME=user`
    * set username: `user`
* `SPRING_BOOT_ADMIN_USER_PASSWORD=password`
    * set password: `password`
* `SPRING_BOOT_ADMIN_TITLE=test`
    * set Page-Title: `test`
* `SPRING_BOOT_ADMIN_SECURITY_ENABLED=false`
    * disable login form (default : `true`)

##### Examples
* `docker run -d -p 8080:8080 -e SPRING_BOOT_ADMIN_TITLE='SB Admin' -e SPRING_BOOT_ADMIN_SECURITY_ENABLED=false fo0me/spring-boot-admin:latest`
* `docker run -d -p 8080:8080 -e SPRING_BOOT_ADMIN_USER_NAME=user -e SPRING_BOOT_ADMIN_USER_PASSWORD='password' fo0me/spring-boot-admin:latest`

Configuration via properties file
---------------------------------
A container supports configuration via *.properties file, just like regular Spring Boot application.
Please note that environment variables will be override by properties file.
Properties of Spring Boot Admin: http://codecentric.github.io/spring-boot-admin/2.1.6/#spring-boot-admin-server

Example `application-docker.properties` file:
```properties
# Spring Boot server port
server.port=8080

# Spring Boot Admin user/uassword
spring.security.user.name=user
spring.security.user.password=password

# Spring Boot Admin title
spring.boot.admin.ui.title=Custom title

# custom property for disable security
spring.boot.admin.security.enabled=false
```

Example command:
- `docker run -d -p 8080:8080 -v "$(pwd)"/application-docker.properties:/opt/spring-boot-admin-docker/application-docker.properties fo0me/spring-boot-admin:latest`

Docker-Compose example
----------------------
Health check usage of `fo0me/spring-boot-admin` with Docker-Compose.
Full working Spring Boot Admin client [here](https://github.com/fo0me/spring-boot-admin-example).
```yaml
version: 3

# admin only
services:
  admin:
    image: fo0me/spring-boot-admin
    environment:
      - SPRING_BOOT_ADMIN_TITLE=Custom Spring Boot Admin title
    volumes:
      - ./application-docker.properties:/opt/spring-boot-admin-docker/application-docker.properties
    ports:
      - 8080:8080
    healthcheck:
      test: "curl -sS http://localhost:1111/health"
      interval: 1s
      timeout: 60s
      retries: 120
    container_name: spring_boot_admin_docker
```


```yaml
version: 3

# admin + client
services:
  example:
    image: <SOME_SPRING_BOOT_ADMIN_CLIENT_IMAGE>
    ports:
      - 8081:8081
    depends_on:
        admin:
          condition: service_healthy
    container_name: spring_boot_admin_example
  admin:
    image: fo0me/spring-boot-admin
    environment:
      - SPRING_BOOT_ADMIN_TITLE=Custom Spring Boot Admin title
    volumes:
      - ./application-docker.properties:/opt/spring-boot-admin-docker/application-docker.properties
    ports:
      - 8080:8080
    healthcheck:
      test: "curl -sS http://localhost:1111/health"
      interval: 1s
      timeout: 60s
      retries: 120
    container_name: spring_boot_admin_docker
```

Links
-----
- Spring Boot Admin documentation: http://codecentric.github.io/spring-boot-admin/2.2.2/
