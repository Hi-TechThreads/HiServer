# Hi Server
[![Gradle](https://github.com/Main-Lib-Squad/HiServer/actions/workflows/gradle.yml/badge.svg)](https://github.com/Main-Lib-Squad/HiServer/actions/workflows/gradle.yml)

This project is a comprehensive microservices architecture designed for high throughput and high availability. The system is composed of several specialized services that work together to deliver robust, scalable, and resilient functionality. Each service is independently deployable and built using Spring Boot and Spring Cloud components.

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Setup & Installation](#setup--installation)
- [Configuration](#configuration)
- [Building and Running](#building-and-running)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Overview

This project demonstrates a microservices-based architecture designed to support high throughput and high availability applications. Its modular design allows each service to be scaled independently, while shared components (such as configuration and common libraries) ensure consistency and ease of management.

Key aspects include:

- **High Throughput:** Optimized for handling a large number of simultaneous requests.
- **High Availability:** Designed with redundancy, load balancing, and service discovery to minimize downtime.
- **Scalability:** Each microservice can be deployed and scaled separately according to demand.
- **Resilience:** Integrated with tools like Eureka for service discovery and Spring Cloud Gateway for API routing and filtering.

## Project Structure

The project uses a multi-module Gradle setup. The primary modules include:

- **[config-server](./config-server):**  
  Centralized configuration server that delivers externalized configuration properties to all microservices.

- **[auth-server](./auth-server):**  
  Authentication service responsible for validating users and issuing tokens for secure communication.

- **[user-server](./user-server):**  
  User management service that handles user-related operations such as registration, profile management, and authorization.

- **[projects](./projects):**
    Including all the projects and practices 
  - **[restaurant](./restaurant):**  
   An example domain service that demonstrates business functionality (e.g., order processing, menu management). Additional domain-specific services can be added similarly.

- **[common](./common):**  
  Contains shared libraries, utilities, and DTOs used across multiple modules.

The root project aggregates the subprojects and manages common dependency configurations and build settings.

## Key Features

- **Centralized Configuration:**  
  Uses Spring Cloud Config Server for externalized configuration management.

- **Service Discovery:**  
  Eureka is used for service registry and discovery, enabling dynamic load balancing and failover.

- **API Gateway:**  
  A dedicated API gateway (built with Spring Cloud Gateway) routes requests, applies custom filters (e.g., rate limiting, whitelisting/blacklisting), and integrates with Eureka for dynamic routing.

- **High Throughput and Resilience:**  
  Microservices are designed to handle a high volume of requests with optimizations at both the networking and service levels.

- **Containerization Ready:**  
  Dockerfiles are provided for each service, facilitating container-based deployments and integration with orchestration platforms like Kubernetes.

## Technologies Used

- **Programming Language:** Java (Version 21 with preview features enabled)
- **Frameworks:** Spring Boot, Spring Cloud, Spring Data JPA, Spring Cloud Gateway, Netflix Eureka
- **Build Tool:** Gradle (with multi-module support)
- **Containerization:** Docker (individual Dockerfiles per microservice)
- **Testing:** JUnit (via Spring Boot Starter Test)
- **Additional Libraries:** Lombok (to reduce boilerplate code)

## Prerequisites

- **Java JDK 21:** Ensure Java 21 (or higher with preview features if required) is installed.
- **Gradle:** Ensure you have Gradle 7 or later installed or use the Gradle Wrapper included in the project.
- **Docker:** (Optional) For containerized deployments.
- **Kubernetes (Optional):** For orchestration of containerized services.
- **Git:** For source code management.

## Setup & Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-org/your-project.git
   cd your-project
   ```
   
2. **Install Dependencies & Build:**

    From the root directory, run:
   ```bash
   ./gradlew clean build
   ```
## Configuration
- **Centralized Configuration:**  
  The ```config-server``` repo manages externalized configuration. Refer to its README for instructions.

- **Service-Specific Configuration:**  
  Each microservice has its own configuration files under ```src/main/resources``` .

