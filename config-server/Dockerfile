FROM gcr.io/distroless/java17
WORKDIR /app
COPY  ./target/*.jar config-server.jar

ENTRYPOINT ["java", "-jar", "config-server.jar"]