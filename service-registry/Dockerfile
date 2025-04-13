# --- Stage 1: Build ---
FROM gradle:8.8.0-jdk17-jammy as builder
WORKDIR /workspace/app

# Copy parent-level Gradle files from the project root.
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Copy the eureka-server subproject files.
COPY service-registry/build.gradle service-registry/build.gradle
COPY service-registry/src service-registry/src

# Build the bootJar task for the eureka-server subproject.
RUN chmod +x gradlew && ./gradlew :service-registry:bootJar --no-daemon -x test --stacktrace --info

# --- Stage 2: Run ---
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built JAR from the builder stage.
COPY --from=builder /workspace/app/service-registry/build/libs/service-registry-*.jar app.jar

EXPOSE 8761

ENV SPRING_PROFILES_ACTIVE=prod \
    EUREKA_CLIENT_REGISTERWITHEUREKA=false \
    EUREKA_CLIENT_FETCHREGISTRY=false \
    EUREKA_HOSTNAME=host.docker.internal \
    EUREKA_SERVER_WAITTIMETOREPLICATION=0

ENTRYPOINT sh -c "java ${JAVA_OPTS:-} -jar -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} /app/app.jar"

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8761/actuator/health || exit 1
