# Build stage
FROM maven:3.9-eclipse-temurin-22 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage (smaller final image)
FROM eclipse-temurin:22-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 9090
CMD ["java", "-jar", "app.jar"]
