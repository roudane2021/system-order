FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY  ./target/*.jar infra-order.jar
ENV SPRING_PROFILE=oracle

ENV BD_ORACLE_HOST=oracle_xe
ENTRYPOINT ["java", "-jar", "infra-order.jar"]