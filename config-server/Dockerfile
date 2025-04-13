# config-server/Dockerfile.local - For Local Development

# --- Stage 1: Build ---
FROM gradle:8.7.0-jdk17-jammy as builder
WORKDIR /workspace/app
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle ../.
COPY build.gradle ../build.gradle
COPY ./config-server/build.gradle ./build.gradle
RUN ./gradlew :config-server:dependencies --no-daemon || true
COPY ./config-server/src ./src
# COPY ./config-server/config-repo ./config-repo # Copy if needed during build, but volume mount is more common
RUN chmod +x ./gradlew && ./gradlew :config-server:bootJar --no-daemon -x test

# --- Stage 2: Run ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /workspace/app/build/libs/config-server-*.jar app.jar

EXPOSE 8888
# EXPOSE 5007 # Optional debug port

# ENV VARS primarily set by Docker Compose
ENV SPRING_PROFILES_ACTIVE="local,git,native" \ # Example default profile combination for local
    EUREKA_URI="http://eureka-server:8761/eureka" \
    # Git URI will be set by compose (using file:// and volume mount)
    SPRING_CLOUD_CONFIG_SERVER_GIT_URI="file:///config-repo" # Default assuming volume mount

ENTRYPOINT ["java", ${JAVA_OPTS:-}, "-jar", \
            "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
            "-Dspring.cloud.config.server.git.uri=${SPRING_CLOUD_CONFIG_SERVER_GIT_URI}", \
            "-Deureka.client.serviceUrl.defaultZone=${EUREKA_URI}", \
            "/app/app.jar"]

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8888/actuator/health || exit 1