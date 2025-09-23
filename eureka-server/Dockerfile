FROM gcr.io/distroless/java17
WORKDIR /app
COPY  ./target/*.jar eureka-server.jar

ENTRYPOINT ["java", "-jar", "eureka-server.jar"]