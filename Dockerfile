# ---------- Build stage ----------
FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

# Copy only what is needed for dependency resolution first
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle gradle

RUN ./gradlew dependencies --no-daemon

# Now copy source
COPY src src

# Build the application
RUN ./gradlew bootJar --no-daemon

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy only the built jar
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose backend port
EXPOSE 1001

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]