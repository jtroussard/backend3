# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy the Maven wrapper and dependencies first for caching optimization
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy the source code and build the JAR
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Create a lightweight runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/backend3-*.jar app.jar

# Expose the correct port for Cloud Run (8080 by default)
EXPOSE 8080

# Use a non-root user for security
RUN useradd -m appuser
USER appuser

# Environment variable for active Spring profile (default to 'local' if not set)
ARG SPRING_PROFILES_ACTIVE=local
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

# Google Cloud Secret Manager Authentication (only in dev/prod)
ENV GCP_PROJECT_ID=""
ENV GOOGLE_APPLICATION_CREDENTIALS="/app/gcp-key.json"

# Copy Google Cloud Service Account JSON key (for dev/prod deployments)
COPY backend3-sql-access-key.json /app/gcp-key.json

# Start the Spring Boot application with profile-based config
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
