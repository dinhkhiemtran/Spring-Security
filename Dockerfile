# Start with an official Maven image to build the app
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file to download dependencies
COPY pom.xml .

# Download project dependencies without the source code
RUN mvn dependency:go-offline -B

# Copy the entire source code into the container
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Start from a smaller base image for running the app
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the packaged JAR from the build stage
COPY --from=build /app/target/security-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
