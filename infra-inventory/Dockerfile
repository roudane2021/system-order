# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml files.
# Copy parent first if it exists and is a relative path, to leverage Docker layer caching.
# This assumes a common parent directory structure like project-root/system-order and project-root/infra-inventory
COPY ../system-order/pom.xml ./system-order/pom.xml
COPY ../domain-inventory/pom.xml ./domain-inventory/pom.xml
COPY pom.xml .

# Copy the rest of the source code for domain and infra modules
# We need domain-inventory because it's a dependency
COPY ../domain-inventory/src ./domain-inventory/src
COPY src ./src

# Package the application (skip tests for faster build in Docker, tests should run in CI pipeline)
# Ensure the system-order (parent) is correctly referenced if needed for the build.
# The `install` command will build domain-inventory and make it available in local .m2 repo
# Then `package` will build infra-inventory.
# Adding -Dmaven.test.skip=true to skip tests as they are not needed for the image.
# Adding -Dmaven.main.skip -Dspring-boot.repackage.skip=true for install phase on parent/dependencies if they are not apps themselves.
RUN \
    (if [ -f ./system-order/pom.xml ]; then mvn -f ./system-order/pom.xml clean install -Dmaven.test.skip=true -Dmaven.main.skip -Dspring-boot.repackage.skip=true; else echo "Parent POM system-order/pom.xml not found, skipping build."; fi) && \
    mvn -f ./domain-inventory/pom.xml clean install -Dmaven.test.skip=true && \
    mvn clean package -Dmaven.test.skip=true

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the jar file from the build stage
# The JAR file will be in target/infra-inventory-*.jar based on artifactId and version from pom.xml
# We need to dynamically find this name or use a fixed one if possible.
# For now, assuming artifactId is 'infra-inventory' and version is picked up.
COPY --from=build /app/target/infra-inventory-*.jar app.jar

# Expose the port the application runs on (if any, for event-driven usually not needed for external HTTP)
# Default for Spring Boot is 8080, but this service is primarily event-driven.
# EXPOSE 8080

# Command to run the application
# Java options can be added here, e.g., for memory limits, Spring profiles
# ENTRYPOINT ["java", "-jar", "app.jar"]
# Adding profile selection example (can be overridden by K8s config)
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar", "--spring.profiles.active=oracle"]
