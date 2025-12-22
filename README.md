# 🍽️ Restaurant Management System API

A complete restaurant management system backend built with Java/Spring Boot.

## 📋 Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose

## 🚀 Quick Start

### 1. Start Database (Docker)

```bash
# Start PostgreSQL and pgAdmin
docker compose up -d

# Check if containers are running
docker compose ps

# View logs
docker compose logs -f postgres
```

### 2. Access Services

| Service | URL | Credentials |
|---------|-----|-------------|
| PostgreSQL | `localhost:5432` | `restaurant` / `restaurant123` |
| pgAdmin | http://localhost:5050 | `admin@restaurant.com` / `admin123` |
| API | http://localhost:8080 | - |
| Swagger UI | http://localhost:8080/swagger-ui.html | - |

### 3. Connect pgAdmin to PostgreSQL

1. Open http://localhost:5050
2. Login with `admin@restaurant.com` / `admin123`
3. Right-click "Servers" → "Register" → "Server"
4. General tab: Name = `Restaurant DB`
5. Connection tab:
   - Host: `postgres` (or `host.docker.internal` on Mac/Windows)
   - Port: `5432`
   - Database: `restaurant_db`
   - Username: `restaurant`
   - Password: `restaurant123`
6. Click "Save"

### 4. Run the Application

```bash
# Using Maven
./mvnw spring-boot:run

# Or with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 5. Test the API

```bash
# Health check
curl http://localhost:8080/actuator/health

# API docs
open http://localhost:8080/swagger-ui.html
```

## 🐳 Docker Commands

```bash
# Start all services
docker compose up -d

# Stop all services
docker compose down

# Stop and remove volumes (reset database)
docker compose down -v

# View logs
docker compose logs -f

# Restart a specific service
docker compose restart postgres
```

## 📁 Project Structure

```
restaurant-api/
├── src/main/java/com/caio/restaurant/
│   ├── RestaurantApplication.java
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── dto/             # Data Transfer Objects
│   ├── entity/          # JPA entities
│   ├── repository/      # Spring Data repositories
│   ├── service/         # Business logic
│   ├── security/        # JWT & Security
│   └── exception/       # Custom exceptions
├── src/main/resources/
│   ├── application.yml
│   ├── application-dev.yml
│   ├── application-docker.yml
│   └── db/migration/    # Flyway migrations
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active profile | `dev` |
| `SPRING_DATASOURCE_URL` | Database URL | `jdbc:postgresql://localhost:5432/restaurant_db` |
| `SPRING_DATASOURCE_USERNAME` | Database user | `restaurant` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `restaurant123` |
| `JWT_SECRET` | JWT signing key | (default in config) |

## 📝 API Documentation

Once the application is running, access:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

## 📦 Build

```bash
# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t restaurant-api .
```

## 🚢 Production Deployment

```bash
# Build and run with Docker Compose
docker compose --profile prod up -d
```

---

Made with ☕ by Caio Carvalho
