# PANT Spring API

A Spring Boot REST service for ingesting and persisting air‐quality readings 
pushed from an external Flask service. It also synchronizes static site metadata 
on startup and exposes internal CRUD for site management.

Developed by Ross Cochrane as part of a submission for MSc Software Development
dissertation.
Part of Pollution Avoidance Navigation Tool (PANT):
Flask Pollution Data Generation Web Service
Spring API (This)
Flask API
Flutter Front End

---

## Table of Contents

1. [Features](#features)
2. [Prerequisites](#prerequisites)
3. [Getting Started](#getting-started)
4. [Configuration](#configuration)
5. [Running with Docker](#running-with-docker)
6. [API Endpoints](#api-endpoints)
7. [Internal Endpoints](#internal-endpoints)
8. [Testing](#testing)


---

## Features

- Subscribes on startup to dynamic pollution data pushes
- Persists only the latest reading per site (upsert logic)
- Fetches and upserts static site metadata without losing historical readings
- JPA entities for `Site` (with PostGIS Point) and `DynamicReading`
- Clean service-repository layering and DTO mapping

---

## Prerequisites

- Java 17+
- Maven (wrapper included: `./mvnw`)
- PostgreSQL 12+ with PostGIS extension
- ngrok or another tunnel for local Flask service

---

## Getting Started

1. Clone the repo
   ```bash
   git clone https://github.com/GarminFenix/PANTspringapi.git
   cd springapi
   ```
2. Set environment variables in PowerShell before running
   $env:DB_USERNAME = "your_username"
   $env:DB_PASSWORD = "your_password"
   $env:NGROK_URL_WEB_SERVICE = "your_web_service_ngrok"
   $env:NGROK_URL_SPRING_SUB = "your_spring_sub_nrgok"
   .\mvnw spring-boot:run 

---

## Configuration

### 1. application.properties
This project uses environment variables for database and NGROK URLS.
These are accessed via ${...} in application properties and System.getenv()
in NgrokConfig.java.

Required Environment Variables
DB_USERNAME     in     application.properties
DB_PASSWORD     in      application.properties

NGROK_URL_WEB_SERVICE   in   NgrokConfig.java
NGROK_URL_SPRING_SUB    in  NgrokConfig.java

No IDE configuration or .env file is required, just set above in shell before running.

---

## Running with Docker

- Build images and bring up services via `docker-compose`:
  ```bash
  docker-compose up --build
  ```
- Services:
    - Spring API app
    - PostgreSQL + PostGIS

See `README.Docker.md` for full Docker instructions.

---

## API Endpoints

These correspond to user stories and are documented elsewhere:
POST    /api/pollution/receive      Ingest dynamic pollution readings from web service (subscription) 

---

## Internal Endpoints

*Omitted from the public API design (internal/admin use only):*
GET     /api/sites                      List site metadata
GET     /api/sites/{systemCodeNumber}   Retrieves one site
POST    /api/sites                      Create or updates a site
DELETE  /api/sites/{systemCodeNumber}   Delete a site

---

## Testing

- **Unit & slice tests** with JUnit 5, Mockito, Spring MVC Test, `@DataJpaTest`.
- Run all tests via:
  ```bash
  ./mvnw test
  ```  

---
