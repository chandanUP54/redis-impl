# Use a base image that contains OpenJDK 22 (latest Java version)
FROM openjdk:22-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the target folder to the container's working directory
COPY target/websocket-application-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (default port for Spring Boot)
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]