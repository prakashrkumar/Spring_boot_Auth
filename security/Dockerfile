# ==============================
# Stage 1: Build Stage
# ==============================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build Spring Boot application
RUN mvn clean package -DskipTests


# ==============================
# Stage 2: Run Stage
# ==============================
FROM eclipse-temurin:21-jre

WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=prod

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
