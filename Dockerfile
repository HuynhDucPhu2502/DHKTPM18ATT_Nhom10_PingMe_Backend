FROM amazoncorretto:21
WORKDIR /app
COPY target/PingMe-Backend-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "PingMe-Backend-0.0.1-SNAPSHOT.jar"]