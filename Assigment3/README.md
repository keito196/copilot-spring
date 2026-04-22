# User Management API - Spring Boot Application

## 📋 Project Overview

This is a Spring Boot REST API application for managing users with complete CRUD operations. The application follows the MVC (Model-View-Controller) architecture pattern and includes centralized exception handling, input validation, and comprehensive API documentation.

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── controller/
│   │   │   └── UserController.java          # REST API endpoints
│   │   ├── service/
│   │   │   ├── UserService.java             # Service interface
│   │   │   └── impl/
│   │   │       └── UserServiceImpl.java      # Service implementation
│   │   ├── repository/
│   │   │   └── UserRepository.java          # Data access layer with custom queries
│   │   ├── model/
│   │   │   └── User.java                    # JPA Entity with Hibernate annotations
│   │   ├── dto/
│   │   │   └── UserDTO.java                 # Data Transfer Object with validation
│   │   ├── exception/
│   │   │   ├── GlobalExceptionHandler.java  # Centralized exception handling
│   │   │   ├── ResourceNotFoundException.java
│   │   │   ├── ValidationException.java
│   │   │   └── ErrorResponse.java           # Standardized error response
│   │   ├── config/
│   │   └── DemoApplication.java             # Main application entry point
│   └── resources/
│       ├── application.properties           # Application configuration
│       ├── static/                          # Static resources (CSS, JS, images)
│       ├── templates/                       # HTML templates
│       └── config/liquibase/
│           └── init_users_data.sql          # Initial database schema and sample data
└── test/
    └── java/com/example/demo/
        └── DemoApplicationTests.java        # Unit tests

pom.xml                                       # Maven dependencies and build configuration
```

## 🛠️ Technology Stack

- **Framework**: Spring Boot 4.0.5
- **Language**: Java 21
- **Database**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **Annotations**: Lombok (to reduce boilerplate)
- **Validation**: Jakarta Bean Validation
- **Web**: Spring MVC/REST

## 📦 Dependencies

- `spring-boot-starter-data-jpa`: JPA/Hibernate support
- `spring-boot-starter-validation`: Bean validation
- `spring-boot-starter-webmvc`: REST API support
- `postgresql`: PostgreSQL driver
- `lombok`: Boilerplate reduction
- `spring-boot-starter-test`: Testing framework

## ✅ Prerequisites

Before running the application, ensure you have:

- **Java 21** or higher installed
- **Maven 3.6+** installed
- **PostgreSQL 12+** installed and running
- **Postman** (for API testing)

## 🚀 Installation & Running the Application

### Step 1: Clone or Setup the Project

```bash
cd /Users/ito/Study/GithubCopilot_SpringBoot/Assigment/copilot-spring/Assigment3
```

### Step 2: Configure Database

Edit `src/main/resources/application.properties` to configure PostgreSQL connection:

```properties
spring.application.name=demo
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.datasource.url=jdbc:postgresql://localhost:5432/user_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

**Note**: Replace `your_password` with your PostgreSQL password.

### Step 3: Create Database (Optional)

If the database doesn't exist, create it:

```bash
psql -U postgres
CREATE DATABASE user_db;
\q
```

### Step 4: Build the Project

```bash
mvn clean install
```

### Step 5: Run the Application

```bash
mvn spring-boot:run
```

Or using the JAR file:

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

### Step 6: Verify Application is Running

Test with a simple request:

```bash
curl http://localhost:8080/api/users
```

## 📚 API Endpoints

All endpoints are prefixed with `/api/users`

### 1. Create a New User
**Request**:
```http
POST /api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

**Response** (201 Created):
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

### 2. Get All Users
**Request**:
```http
GET /api/users
```

**Response** (200 OK):
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "USER"
  },
  {
    "id": 2,
    "name": "Jane Admin",
    "email": "jane@example.com",
    "password": "password456",
    "role": "ADMIN"
  }
]
```

### 3. Get User by ID
**Request**:
```http
GET /api/users/{id}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "USER"
}
```

**Error** (404 Not Found):
```json
{
  "timestamp": "2026-04-22T10:30:45.123456",
  "status": 404,
  "message": "User not found with id: 999",
  "path": "/api/users/999"
}
```

### 4. Update User
**Request**:
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "password": "newpassword123",
  "role": "ADMIN"
}
```

**Response** (200 OK):
```json
{
  "id": 1,
  "name": "John Updated",
  "email": "john.updated@example.com",
  "password": "newpassword123",
  "role": "ADMIN"
}
```

### 5. Delete User
**Request**:
```http
DELETE /api/users/{id}
```

**Response** (204 No Content) - No body returned

## ❌ Error Handling

The application implements centralized exception handling with standardized JSON error responses.

### Validation Error Example

**Request with invalid email**:
```http
POST /api/users
Content-Type: application/json

{
  "name": "Test",
  "email": "invalid-email",
  "password": "pass",
  "role": "INVALID"
}
```

**Response** (400 Bad Request):
```json
{
  "timestamp": "2026-04-22T10:30:45.123456",
  "status": 400,
  "message": "Validation failed",
  "path": "/api/users",
  "details": {
    "email": "Email should be valid",
    "password": "Password must be at least 6 characters",
    "role": "Role must be ADMIN or USER"
  }
}
```

## 📮 Postman Collection Setup

### Quick Start: Import Postman Collection

The project includes a pre-configured Postman collection file with all CRUD endpoints set up with variables and tests.

#### Steps to Import:

1. **Open Postman**

2. **Import Collection**:
   - Click **"File"** → **"Import"**
   - Select the file: `REST API basics- CRUD, test & variable.postman_collection.json`
   - Click **"Import"**

3. **Create/Configure Environment**:
   - Click **"Environments"** (left sidebar)
   - Click **"Create new"** or select an existing environment
   - Add the following environment variables:
     ```
     base_url = http://localhost:8080
     api_path = /api/users
     ```
   - Click **"Save"**

4. **Select Environment**:
   - In the top-right corner, select your environment from the dropdown (e.g., "Local")

5. **Run Requests**:
   - Expand the collection in the left sidebar
   - Click on any request and click **"Send"**

#### Available Requests in Collection:

- **Create User** (POST) - Creates a new user
- **Get All Users** (GET) - Retrieves all users
- **Get User by ID** (GET) - Retrieves a specific user
- **Update User** (PUT) - Updates an existing user
- **Delete User** (DELETE) - Deletes a user

#### Testing in Postman:

The collection includes automated test scripts that verify:
- Correct HTTP status codes
- Response structure
- Required fields in responses
- Error message format

Run all tests:
1. Select the collection
2. Click the **"Run"** button
3. Execute the collection runner to run all requests with tests

## 🗄️ Database Schema

The `users` table is created with the following structure:

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

Sample data is automatically inserted on first run (see `src/main/resources/config/liquibase/init_users_data.sql`)

## 🔐 Security Notes

⚠️ **Important**: 
- Passwords in the current implementation are NOT encrypted. For production, implement BCrypt or similar hashing.
- Passwords should NOT be returned in API responses.
- Implement authentication/authorization (JWT, OAuth2, etc.) for production.
- Use HTTPS for all communications.

## 📝 Validation Rules

- **Name**: Maximum 100 characters, required
- **Email**: Valid email format, unique, required
- **Password**: Minimum 6 characters, required
- **Role**: Must be either "ADMIN" or "USER", required

## 🧪 Testing Tips

1. Test with invalid email format
2. Test with duplicate email
3. Test with missing required fields
4. Test with invalid role values
5. Test with non-existent user IDs
6. Test password minimum length validation

## 📞 Support & Troubleshooting

### Common Issues

**Issue**: PostgreSQL connection refused
- **Solution**: Ensure PostgreSQL is running: `pg_isready -U postgres -h localhost`

**Issue**: Port 8080 already in use
- **Solution**: Change port in `application.properties`: `server.port=8081`

**Issue**: Validation errors in Postman
- **Solution**: Check field constraints in UserDTO class

## 📄 License

This project is provided as-is for educational purposes.

---

**Last Updated**: April 22, 2026  
**Java Version**: 21  
**Spring Boot Version**: 4.0.5


