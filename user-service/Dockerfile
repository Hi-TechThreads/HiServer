# Use a base image with Java and any necessary tools
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the packaged jar file (assuming maven builds it in target/)
# The JAR name might vary based on your pom.xml artifactId/version
COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on (as defined in application.yml/config server)
EXPOSE 8081

# Command to run the application
# Use environment variables for dynamic configuration (DB creds, Eureka URL, Config Server URL)
ENTRYPOINT ["java", "-jar", \
            "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:default}", \
            "-Deureka.client.serviceUrl.defaultZone=${EUREKA_URI}", \
            "-Dspring.cloud.config.uri=${CONFIG_SERVER_URI}", \
            # Add DB creds, Kafka brokers, Redis host etc. as needed
             "-Dspring.datasource.url=${DB_URL}", \
             "-Dspring.datasource.username=${DB_USER}", \
             "-Dspring.datasource.password=${DB_PASS}", \
            "/app/app.jar"]

# Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1
