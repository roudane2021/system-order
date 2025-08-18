FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY  ./target/*.jar infra-notification.jar

ENTRYPOINT ["java", "-jar", "infra-notification.jar"]