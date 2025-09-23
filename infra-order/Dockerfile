FROM gcr.io/distroless/java17
COPY ./target/*.jar app.jar
ENV SPRING_PROFILE=oracle

ENV BD_ORACLE_HOST=oracle_xe
ENTRYPOINT ["java", "-jar", "/app.jar"]