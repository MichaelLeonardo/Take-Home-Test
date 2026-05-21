# Leave Management API

Backend API project built with Spring Boot, JWT Authentication, PostgreSQL, and Docker.

---

# Tech Stack

* Java 21
* Spring Boot 3
* Spring Security
* JWT Authentication
* PostgreSQL
* Docker & Docker Compose
* Swagger / OpenAPI

---

# Default Seeded Users

The application already provides default users and roles through Seeder.

Available roles:

* Superadmin
* Manager
* Employee

Default users are automatically created when the application starts.

Credentials are configured using environment variables in `.env`.

Example:

```env
SUPERADMIN_USERNAME=superadmin
SUPERADMIN_PASSWORD=your-password

MANAGER_USERNAME=manager
MANAGER_PASSWORD=your-password

EMPLOYEE_USERNAME=employee
EMPLOYEE_PASSWORD=your-password
```

---

# Features

* User Login Authentication
* JWT Authorization
* Dynamic Permission Access
* Leave Request Submission
* Leave Approval
* Role-Based Access Control
* Swagger API Documentation
* Dockerized Environment

---

# Project Structure

```text
src/main/java
├── auth
├── config
├── controller
├── dto
├── entity
├── repository
├── service
└── util
```

---

# Setup Project

## 1. Clone Repository

```bash
git clone <repository-url>
cd <project-folder>
```

---

# Environment Variables

Create `.env` file:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/takehome
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
```

---

# Run with Docker

## Build Project

```bash
mvn clean package
```

## Start Docker Compose

```bash
docker compose up --build
```

Application:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Docker Compose Example

```yaml
services:

  postgres:
    image: postgres:16
    container_name: postgres-db
    restart: always
    environment:
      POSTGRES_DB: takehome
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"

  app:
    build: .
    container_name: spring-app
    restart: always
    depends_on:
      - postgres
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/takehome
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
```

---

# JWT Authentication

## Login Endpoint

```http
POST /auth/login
```

Example Request:

```json
{
  "username": "john",
  "password": "password123"
}
```

Example Response:

```json
{
  "status": "OK",
  "message": "Login Successfully",
  "data": "jwt-token"
}
```

---

# Authorization

Authorization uses JWT Bearer Token.

Example Header:

```http
Authorization: Bearer your-jwt-token
```

Permissions are stored inside JWT claims.

Example:

```json
{
  "sub": "john",
  "permissions": [
    "/leave/**",
    "/user/view"
  ]
}
```

---

# API Endpoints

## 👤 User Management

| Method | Endpoint                              | Description                         | Auth             |
| ------ | ------------------------------------- | ----------------------------------- | ---------------- |
| POST   | /user/login                           | User login and generate JWT token   | Public           |
| POST   | /user/register                        | Register new user                   | Public           |
| PUT    | /user/reset-password                  | Reset user password                 | Auth             |
| PUT    | /user                                 | Edit user profile                   | Auth             |
| DELETE | /user/{username}                      | Delete user by username             | Admin/Superadmin |
| PATCH  | /user/{username}?is_active=true/false | Activate / deactivate user          | Admin/Superadmin |
| GET    | /user                                 | Get all users (pagination + search) | Auth             |
| GET    | /user/{username}                      | Get user detail                     | Auth             |

---

## 🧾 Leave Management

### 📌 Apply Leave

| Method | Endpoint             | Description          | Role              |
| ------ | -------------------- | -------------------- | ----------------- |
| POST   | /leave/request/apply | Submit leave request | Employee, Manager |

**Request Params:**

* date_from (yyyy-MM-dd)
* date_to (yyyy-MM-dd)
* reason

---

### 📌 Leave History (Personal)

| Method | Endpoint             | Description                | Role              |
| ------ | -------------------- | -------------------------- | ----------------- |
| GET    | /leave/request/apply | Get personal leave history | Employee, Manager |

**Filters:**

* page
* limit
* sort_by
* order
* date_from
* date_to
* status

---

### 📌 Leave Approval

| Method | Endpoint                          | Description                            | Role                |
| ------ | --------------------------------- | -------------------------------------- | ------------------- |
| POST   | /leave/approval/apply/{id}        | Approve leave request                  | Manager, Superadmin |
| POST   | /leave/approval/apply/reject/{id} | Reject leave request                   | Manager, Superadmin |
| GET    | /leave/approval/apply             | Get all leave requests (all employees) | Manager, Superadmin |

**Filters (GET approval list):**

* page
* limit
* sort_by
* order
* date_from
* date_to
* status
* employee_id

---

## 🧑‍💼 Role Management

| Method | Endpoint   | Description                         | Role       |
| ------ | ---------- | ----------------------------------- | ---------- |
| POST   | /role      | Create new role with features       | Superadmin |
| PUT    | /role/{id} | Edit role features                  | Superadmin |
| GET    | /role      | Get all roles (pagination + search) | Superadmin |

---

# Run Without Docker

## Start PostgreSQL

Make sure PostgreSQL is running locally.

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/takehome
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Run Spring Boot

```bash
mvn spring-boot:run
```

---

# Build Jar

```bash
mvn clean package
```

Generated jar:

```text
target/*.jar
```

Run jar:

```bash
java -jar target/app.jar
```

---

# Swagger Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

---

# Future Improvements

* Refresh Token
* Role & Permission Management
* Redis Cache
* API Rate Limiting
* Unit Testing
* CI/CD Pipeline
* Kubernetes Deployment

---

# Author

Michael Leonardo Ghozali
