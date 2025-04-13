# --- Stage 1: Build ---
FROM gradle:8.8.0-jdk17-jammy as builder
WORKDIR /workspace/app

# Copy parent-level Gradle files from the project root.
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Copy the eureka-server subproject files.
COPY user-service/build.gradle user-service/build.gradle
COPY user-service/src user-service/src

# Build the bootJar task for the eureka-server subproject.
RUN chmod +x gradlew && ./gradlew :user-service:bootJar --no-daemon -x test --stacktrace --info

# --- Stage 2: Run ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built JAR from the builder stage.
COPY --from=builder /workspace/app/user-service/build/libs/user-service-*.jar app.jar

# Expose the port the application runs on (as defined in application.yml/config server)
EXPOSE 8081

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT sh -c "java ${JAVA_OPTS:-} -jar -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} /app/app.jar"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1
