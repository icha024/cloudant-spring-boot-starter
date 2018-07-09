FROM openjdk:8-jdk-alpine
COPY target/cloudant-spring-boot-starter-example-2.0.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]