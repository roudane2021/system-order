FROM gcr.io/distroless/java17
WORKDIR /app
COPY  ./target/*.jar infra-inventory.jar
ENV SPRING_PROFILE=oracle

ENV BD_ORACLE_HOST=oracle_xe
ENTRYPOINT ["java", "-jar", "infra-inventory.jar"]