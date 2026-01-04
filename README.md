# Auth-Service

A Spring Boot-based authentication service that provides JWT-based authentication with refresh token support. This service handles user signup, login, and token refresh operations.

## Features

- 🔐 User Registration (Signup)
- 🔑 User Login with JWT Authentication
- 🔄 Refresh Token Mechanism
- 🛡️ Spring Security Integration
- 💾 MySQL Database Integration
- 📝 JWT Token Generation and Validation

## Tech Stack

- **Java 21**
- **Spring Boot 3.3.0**
- **Spring Security**
- **Spring Data JPA**
- **MySQL 8.0**
- **JWT (JSON Web Tokens)**
- **Lombok**
- **Gradle**

## Prerequisites

- Java 21 or higher
- MySQL 8.0 or higher
- Gradle 9.2.1 or higher

## Configuration

### Database Setup

Update the database configuration in `app/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:127.0.0.1}:${MYSQL_PORT:3306}/${MYSQL_DB:authservice}
spring.datasource.username=root
spring.datasource.password=root123
```

### Environment Variables (Optional)

- `MYSQL_HOST` - MySQL host (default: 127.0.0.1)
- `MYSQL_PORT` - MySQL port (default: 3306)
- `MYSQL_DB` - Database name (default: authservice)

### Server Port

The application runs on port **9898** by default. You can change it in `application.properties`:

```properties
server.port=9898
```

## Building and Running

### Build the Project

```bash
./gradlew build
```

### Run the Application

```bash
./gradlew bootRun
```

Or:

```bash
java -jar app/build/libs/app.jar
```

## API Endpoints

Base URL: `http://localhost:9898`

### 1. User Signup

Register a new user account.

**Endpoint:** `POST /auth/v1/signup`

**Request Body:**
```json
{
  "user_id": "user123",
  "username": "john_doe",
  "password": "securePassword123",
  "first_name": "John",
  "last_name": "Doe",
  "phone_number": 1234567890,
  "email": "john.doe@example.com"
}
```

**Success Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token": "abc123-def456-ghi789"
}
```

**Error Responses:**
- `400 Bad Request` - User already exists: `"Already Exist"`
- `500 Internal Server Error` - Server error: `"Exception in User Service"`

---

### 2. User Login

Authenticate an existing user and receive JWT tokens.

**Endpoint:** `POST /auth/v1/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Success Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token": "abc123-def456-ghi789"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid credentials
- `500 Internal Server Error` - Server error: `"Exception in User Service"`

---

### 3. Refresh Token

Get a new access token using a valid refresh token.

**Endpoint:** `POST /auth/v1/refreshToken`

**Request Body:**
```json
{
  "token": "abc123-def456-ghi789"
}
```

**Success Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token": "abc123-def456-ghi789"
}
```

**Error Responses:**
- `401 Unauthorized` - Token not found: `"Refresh Token is not in DB"`
- `401 Unauthorized` - Token expired: `"<token> Refresh token is expired. Please make a new login..!"`
- `500 Internal Server Error` - Server error: `"Exception in Refresh Token Service"`

---

## Using the API

### Example: Signup Request (Postman/cURL)

**Postman:**
1. Method: `POST`
2. URL: `http://localhost:9898/auth/v1/signup`
3. Headers:
   - `Content-Type: application/json`
4. Body (raw JSON):
   ```json
   {
     "user_id": "user123",
     "username": "john_doe",
     "password": "securePassword123",
     "first_name": "John",
     "last_name": "Doe",
     "phone_number": 1234567890,
     "email": "john.doe@example.com"
   }
   ```

**cURL:**
```bash
curl -X POST http://localhost:9898/auth/v1/signup \
  -H "Content-Type: application/json" \
  -d '{
    "user_id": "user123",
    "username": "john_doe",
    "password": "securePassword123",
    "first_name": "John",
    "last_name": "Doe",
    "phone_number": 1234567890,
    "email": "john.doe@example.com"
  }'
```

### Example: Login Request

**Postman:**
1. Method: `POST`
2. URL: `http://localhost:9898/auth/v1/login`
3. Headers:
   - `Content-Type: application/json`
4. Body (raw JSON):
   ```json
   {
     "username": "john_doe",
     "password": "securePassword123"
   }
   ```

**cURL:**
```bash
curl -X POST http://localhost:9898/auth/v1/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "securePassword123"
  }'
```

### Example: Refresh Token Request

**Postman:**
1. Method: `POST`
2. URL: `http://localhost:9898/auth/v1/refreshToken`
3. Headers:
   - `Content-Type: application/json`
4. Body (raw JSON):
   ```json
   {
     "token": "abc123-def456-ghi789"
   }
   ```

**cURL:**
```bash
curl -X POST http://localhost:9898/auth/v1/refreshToken \
  -H "Content-Type: application/json" \
  -d '{
    "token": "abc123-def456-ghi789"
  }'
```

## Security

- All endpoints (`/auth/v1/signup`, `/auth/v1/login`, `/auth/v1/refreshToken`) are publicly accessible (no authentication required)
- Other endpoints require JWT authentication via `Authorization: Bearer <token>` header
- Passwords are encrypted using BCrypt
- JWT tokens are used for authenticated requests
- Refresh tokens expire after 7 days

## Project Structure

```
app/src/main/java/org/example/authservice
├── auth/
│   ├── JwtAuthFilter.java          # JWT authentication filter
│   └── SecurityConfig.java         # Spring Security configuration
├── controller/
│   ├── AuthController.java         # Signup endpoint
│   └── TokenController.java        # Login and refresh token endpoints
├── entities/
│   ├── RefreshToken.java           # Refresh token entity
│   ├── UserInfo.java               # User entity
│   └── UserRole.java               # User role entity
├── model/
│   └── UserInfoDto.java            # User DTO for signup
├── repository/
│   ├── RefreshTokenRepository.java # Refresh token repository
│   └── UserRepository.java         # User repository
├── request/
│   ├── AuthRequestDto.java         # Login request DTO
│   └── RefreshTokenRequestDto.java # Refresh token request DTO
├── response/
│   └── JwtResponseDto.java         # JWT response DTO
└── service/
    ├── JwtService.java              # JWT token service
    ├── RefreshTokenService.java     # Refresh token service
    └── UserDetailsServiceImpl.java  # User details service
```

## Database Schema

The application uses JPA/Hibernate with `ddl-auto=update`, which automatically creates/updates the database schema. Main tables:

- `users` - User information
- `tokens` - Refresh tokens
- `users_roles` - User roles mapping

## Logging

Security debug logging is enabled. To adjust logging levels, modify `application.properties`:

```properties
logging.level.org.springframework.security=DEBUG
```

## License

This project is part of an example authentication service implementation.
