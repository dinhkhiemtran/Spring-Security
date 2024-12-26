# Project Name

This project is a Spring Boot application that integrates Spring Security, H2 Database, JWT (JSON Web Token), and Swagger for API documentation. The application demonstrates how to secure endpoints with JWT, manage user authentication and authorization, use an in-memory H2 database for development, and document APIs with Swagger.

## Table of Contents

- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [H2 database](#running-h2-database)
- [API Documentation](#api-documentation)

## Introduction

This project showcases the implementation of a secure RESTful API using Spring Security and JWT for authentication and authorization. It uses H2 as the in-memory database for easy setup and Swagger for API documentation.

## Technologies Used

- **Spring Boot**: Framework for building Java-based applications.
- **Spring Security**: Provides authentication and authorization capabilities.
- **H2 Database**: In-memory database for development and testing.
- **JWT (JSON Web Token)**: Standard for securing API endpoints.
- **Swagger**: Tool for API documentation.

## Setup and Installation

1. **Clone the Repository**
    ```bash
    git clone https://github.com/dinhkhiemtran/Spring-Security.git
    ```

2. **Build the Project**
   Make sure you have JDK 21 or later installed.
    ```bash
    ./mvnw clean install
    ```

3. **Run the Application**
    ```bash
    ./mvnw spring-boot:run
    ```

## Technologies Used

- **Spring Boot**: Framework for building Java-based applications.
- **Spring Security**: Provides authentication and authorization capabilities.
- **H2 Database**: In-memory database for development and testing.
- **JWT (JSON Web Token)**: Standard for securing API endpoints.
- **Swagger**: Tool for API documentation.

## Setup and Installation

1. **Clone the Repository**
    ```bash
    git clone https://github.com/dinhkhiemtran/Spring-Security.git
    ```

2. **Build the Project**
   Make sure you have JDK 11 or later installed.
    ```bash
    ./mvnw clean install
    ```

3. **Run the Application**
    ```bash
    ./mvnw spring-boot:run
    ```

## Configuration

### Spring Security

Spring Security is configured to secure the endpoints of the application. JWT is used for authenticating users. The security configurations are in the `SecurityConfig` class.

```yaml
### Spring Security
spring:
  output.ansi.enabled: always
openapi:
  service:
    title: "API Spring Security Service Document"
    version: "v1.0.0"
    description: "Api security service for {profile}"
    license:
      name: "API License"
      identifier: "MIT"
      url: "https://{profile}-server:8080/api-docs"
logging:
  level:
    root: INFO
    org.springframework.web: INFO
  pattern.console: "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m %X{Remote_Address} %clr(%n%wEx){red}"
```

### Running the Application

```bash
 ./mvnw spring-boot:run
```

### H2 Database
The application uses H2 as the in-memory database. The H2 console is available at `/h2-console`. Configure the H2 database settings in `application.yaml`:

 http://localhost:8080/h2-console

### API Documentation

Swagger provides interactive API documentation. You can test the API endpoints directly from the Swagger UI.

 http://localhost:8080/swagger-ui/index.html

# Guide to activating profile

### Activate via applciation.yaml
```yaml 
spring:
  profiles:
    active: local
```
### Activate via command line
```bash
java -jar myapp.jar --spring.profiles.active=local
``` 
### Activate via environment variable
#### On Linux/MacOS:
```bash 
export SPRING_PROFILES_ACTIVE=local
```
#### On Windows:
```bash
set SPRING_PROFILES_ACTIVE=local
```

