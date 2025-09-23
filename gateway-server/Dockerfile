FROM gcr.io/distroless/java17
WORKDIR /app
COPY  ./target/*.jar gateway-server.jar

ENTRYPOINT ["java", "-jar", "gateway-server.jar"]