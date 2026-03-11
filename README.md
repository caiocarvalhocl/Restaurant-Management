# 🍽️ Restaurant Management API

A production-ready REST API for restaurant management built with **Java 17** and **Spring Boot 3**. Features complete table management, billing system, and JWT authentication with role-based authorization.

## ✨ Features

- **Authentication & Authorization**
  - JWT-based stateless authentication
  - Role-based access control (Owner, Cashier, Employee)
  - Secure password hashing with BCrypt

- **Restaurant Operations**
  - Table management with status tracking (Available, Occupied, Reserved)
  - Menu management with categories and products
  - Complete billing system with itemized orders

- **Bill Workflow**
  - Open bill → Add items → Apply discounts → Close with payment
  - Automatic table status updates
  - Support for multiple payment methods (Cash, Card, PIX)

## 🛠️ Tech Stack

| Layer                | Technology                  |
| -------------------- | --------------------------- |
| **Language**         | Java 17                     |
| **Framework**        | Spring Boot 3.2             |
| **Security**         | Spring Security + JWT       |
| **Database**         | PostgreSQL 16               |
| **ORM**              | Spring Data JPA / Hibernate |
| **Migrations**       | Flyway                      |
| **Validation**       | Jakarta Bean Validation     |
| **Build**            | Maven                       |
| **Containerization** | Docker & Docker Compose     |

## 📊 API Endpoints

### Authentication

| Method | Endpoint             | Description       | Access |
| ------ | -------------------- | ----------------- | ------ |
| POST   | `/api/auth/register` | Register new user | Public |
| POST   | `/api/auth/login`    | Login and get JWT | Public |

### Categories

| Method | Endpoint               | Description         | Access        |
| ------ | ---------------------- | ------------------- | ------------- |
| GET    | `/api/categories`      | List all categories | Authenticated |
| GET    | `/api/categories/{id}` | Get category by ID  | Authenticated |
| POST   | `/api/categories`      | Create category     | Owner         |
| PUT    | `/api/categories/{id}` | Update category     | Owner         |
| DELETE | `/api/categories/{id}` | Delete category     | Owner         |

### Products

| Method | Endpoint             | Description       | Access        |
| ------ | -------------------- | ----------------- | ------------- |
| GET    | `/api/products`      | List all products | Authenticated |
| GET    | `/api/products/{id}` | Get product by ID | Authenticated |
| POST   | `/api/products`      | Create product    | Owner         |
| PUT    | `/api/products/{id}` | Update product    | Owner         |
| DELETE | `/api/products/{id}` | Delete product    | Owner         |

### Tables

| Method | Endpoint           | Description     | Access        |
| ------ | ------------------ | --------------- | ------------- |
| GET    | `/api/tables`      | List all tables | Authenticated |
| GET    | `/api/tables/{id}` | Get table by ID | Authenticated |
| POST   | `/api/tables`      | Create table    | Owner         |
| DELETE | `/api/tables/{id}` | Delete table    | Owner         |

### Bills

| Method | Endpoint                         | Description          | Access         |
| ------ | -------------------------------- | -------------------- | -------------- |
| GET    | `/api/bills`                     | List all bills       | Owner, Cashier |
| GET    | `/api/bills/open`                | List open bills      | Authenticated  |
| GET    | `/api/bills/{id}`                | Get bill by ID       | Authenticated  |
| POST   | `/api/bills`                     | Open new bill        | Authenticated  |
| POST   | `/api/bills/{id}/items`          | Add item to bill     | Authenticated  |
| DELETE | `/api/bills/{id}/items/{itemId}` | Remove item          | Authenticated  |
| POST   | `/api/bills/{id}/close`          | Close bill (payment) | Owner, Cashier |
| POST   | `/api/bills/{id}/cancel`         | Cancel bill          | Owner, Cashier |

## 🗄️ Database Schema

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│    users     │     │  categories  │     │   products   │
├──────────────┤     ├──────────────┤     ├──────────────┤
│ id           │     │ id           │     │ id           │
│ name         │     │ name         │     │ name         │
│ email        │     │ description  │     │ description  │
│ password     │     │ created_at   │     │ price        │
│ role         │     └──────────────┘     │ price_type   │
│ active       │            │             │ available    │
│ created_at   │            └─────────────│ category_id  │
│ updated_at   │                          │ created_at   │
└──────────────┘                          └──────────────┘
       │                                         │
       │     ┌──────────────┐                    │
       │     │    tables    │                    │
       │     ├──────────────┤                    │
       │     │ id           │                    │
       │     │ table_number │                    │
       │     │ capacity     │                    │
       │     │ status       │                    │
       │     │ created_at   │                    │
       │     └──────────────┘                    │
       │            │                            │
       │            │                            │
       ▼            ▼                            │
┌──────────────────────────┐                     │
│          bills           │                     │
├──────────────────────────┤                     │
│ id                       │                     │
│ table_id (nullable)      │                     │
│ client_name              │                     │
│ status                   │                     │
│ opened_by / closed_by    │                     │
│ subtotal / discount      │                     │
│ total / payment_method   │                     │
│ opened_at / closed_at    │                     │
└──────────────────────────┘                     │
            │                                    │
            ▼                                    │
┌──────────────────────────┐                     │
│       bill_items         │◄────────────────────┘
├──────────────────────────┤
│ id                       │
│ bill_id                  │
│ product_id               │
│ quantity                 │
│ unit_price / total_price │
│ notes                    │
│ added_by                 │
│ created_at               │
└──────────────────────────┘
```

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose

### 1. Clone and Start

```bash
# Clone the repository
git clone https://github.com/yourusername/restaurant-api.git
cd restaurant-api

# Start PostgreSQL
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

### 2. Test the API

```bash
# Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@test.com","password":"123456","role":"OWNER"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@test.com","password":"123456"}'

# Use the returned token for authenticated requests
curl http://localhost:8080/api/categories \
  -H "Authorization: Bearer <your-token>"
```

### Services

| Service    | URL                   | Credentials                         |
| ---------- | --------------------- | ----------------------------------- |
| API        | http://localhost:8080 | -                                   |
| PostgreSQL | localhost:5432        | `restaurant` / `restaurant123`      |
| pgAdmin    | http://localhost:5050 | `admin@restaurant.com` / `admin123` |

## 📁 Project Structure

```
src/main/java/com/caio/restaurant/
├── config/          # Security and app configuration
├── controller/      # REST API endpoints
├── dto/             # Request/Response objects
│   ├── request/     # Input DTOs with validation
│   └── response/    # Output DTOs
├── entity/          # JPA entities
├── enums/           # Status and role enums
├── exception/       # Custom exceptions & handlers
├── repository/      # Data access layer
├── security/        # JWT filter and service
└── service/         # Business logic
```

## 🔧 Configuration

### Environment Variables

| Variable                     | Description           | Default                                          |
| ---------------------------- | --------------------- | ------------------------------------------------ |
| `SPRING_DATASOURCE_URL`      | Database URL          | `jdbc:postgresql://localhost:5432/restaurant_db` |
| `SPRING_DATASOURCE_USERNAME` | Database user         | `restaurant`                                     |
| `SPRING_DATASOURCE_PASSWORD` | Database password     | `restaurant123`                                  |
| `JWT_SECRET`                 | JWT signing key       | (configured in application.yml)                  |
| `JWT_EXPIRATION`             | Token expiration (ms) | `86400000` (24h)                                 |

## 🐳 Docker Commands

```bash
# Start services
docker compose up -d

# Stop services
docker compose down

# Reset database
docker compose down -v && docker compose up -d

# View logs
docker compose logs -f postgres
```

## 📦 Build & Deploy

```bash
# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t restaurant-api .

# Run with Docker
docker run -p 8080:8080 restaurant-api
```

---

## 👤 Author

**Caio Carvalho**

- GitHub: [@caiocarvalhocl](https://github.com/caiocarvalhocl)
- LinkedIn: [Caio Carvalho](https://linkedin.com/in/caiocarvalhocl)

## 📄 License

This project is licensed under the MIT License.
