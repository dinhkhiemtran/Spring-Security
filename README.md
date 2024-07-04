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
  application:
    name: "spring-security"
    path-allowed:
      - /greeting/**
      - /api/v1/sign-up
      - /api/v1/login
      - /h2-console/**
      - /swagger-ui/**
      - /v3/api-docs/**
    jwt:
      jwt-secret: "1LpA33BCD+ChEC+mrOcu7g+nrAJmyT2qnoxmXuzbK1d9gN2q55ql5bOYTIPJTmJqnplNWFNyIsaQoWUVWDkAugXhCGYqgAhLy/kJ5O64ocWYVCs+OQinaynVGeE0Jh3WXfOqB8TEi42GZSwRbbLy8ea8QUegRJok+KXK9gD8GaA="
      expire-time: "300"
      
### H2 Database      
  h2:
    console:
      enabled: "true"
  datasource:
    url: "jdbc:h2:mem:testdb"
    driverClassName: "org.h2.Driver"
    username: "sa"
    password: "password"

### JPA 
  jpa:
    database-platform: "org.hibernate.dialect.H2Dialect"
    defer-datasource-initialization: true

### SQL
  sql:
    init:
      mode: "always"

### Logging
logging:
  level:
    org:
      springframework:
        web=DEBUG:
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

