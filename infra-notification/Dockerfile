FROM gcr.io/distroless/java17
WORKDIR /app
COPY  ./target/*.jar infra-notification.jar

ENTRYPOINT ["java", "-jar", "infra-notification.jar"]