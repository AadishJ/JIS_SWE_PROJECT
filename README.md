# 🏛️ Judicial Information System (JIS)

A **Spring Boot + MariaDB backend system** for managing judicial case workflows including case creation, hearings, adjournments, and access control.

---

## 🚀 Features

- ⚖️ Case Management (Create, Update, Close)
- 📅 Hearing Scheduling & Adjournments
- 💳 Lawyer Payment System for Case Access
- 🔍 Query Cases (by CIN, date, status)
- 👤 Role-Based Access (Registrar, Judge, Lawyer, Admin)
- 📚 Access Control for Closed Cases

---

## 🧱 Tech Stack

- **Backend:** Spring Boot (Java)
- **Database:** MySQL / MariaDB
- **ORM:** Spring Data JPA (Hibernate)
- **Build Tool:** Maven
- **Utilities:** Lombok

---

## 📂 Project Structure

```
com.jis
 ├── controller   # REST APIs
 ├── service      # Business logic
 ├── repository   # Database access (JPA)
 ├── entity       # JPA entities
 ├── dto          # Request/Response objects
 └── config       # Configuration (future: security)
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone Repository

```bash
git clone <your-repo-url>
cd jis-backend
```

---

### 2️⃣ Configure Database

Create database:

```sql
CREATE DATABASE jis_db;
```

Create the DB user (matches local defaults):

```sql
CREATE USER 'jis_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON jis_db.* TO 'jis_user'@'localhost';
FLUSH PRIVILEGES;
```

Optional: set environment variables (recommended):

```properties
DB_URL=jdbc:mariadb://localhost:3306/jis_db
DB_USER=jis_user
DB_PASSWORD=password123

DDL_AUTO=validate
JPA_SHOW_SQL=true
```

#### Do I need to create tables manually?

- **Default (validate):** Yes — the app will refuse to start unless tables already exist.
- **Local dev (recommended):** No — run with the `dev` profile and Hibernate will create/update tables for you and seed a few users.

Run in dev mode:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Default users (dev profile)

When running with the `dev` profile, the app seeds these users automatically:

- `Registrar1` / password: `pass` / role: `REGISTRAR`
- `Judge1` / password: `pass` / role: `JUDGE`
- `Lawyer1` / password: `pass` / role: `LAWYER`

---

### 3️⃣ Run Application

```bash
./mvnw spring-boot:run
```

Server will start at:

```
http://localhost:8080
```

---

## ☁️ Deploy Online (Recommended: Railway + MariaDB)

This repo is now deployment-ready with:

- `Dockerfile` for container builds
- `application-prod.properties` for production defaults
- Environment-variable driven DB, JWT, and CORS config

### 1️⃣ Push this project to GitHub

Railway will deploy from your GitHub repository.

### 2️⃣ Create a Railway project

- Add a **new service** from your GitHub repo (this backend).
- Add a second service for MariaDB.
  If MariaDB is not listed directly in your Railway UI, create a service from Docker image `mariadb:11`.

### 3️⃣ Configure MariaDB service variables

Set these on the MariaDB service:

- `MARIADB_DATABASE=jis_db`
- `MARIADB_USER=jis_user`
- `MARIADB_PASSWORD=<strong_password>`
- `MARIADB_ROOT_PASSWORD=<strong_root_password>`

### 4️⃣ Configure backend service variables

Set these on the backend service:

```properties
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:mariadb://<mariadb-service-host>:3306/jis_db
DB_USER=jis_user
DB_PASSWORD=<strong_password>
DDL_AUTO=update

JWT_SECRET=<at-least-32-character-secret>
JWT_EXPIRATION_HOURS=8
JWT_COOKIE_SECURE=true
JWT_COOKIE_SAME_SITE=None

CORS_ALLOWED_ORIGINS=https://<your-frontend-domain>
```

Notes:

- Railway provides `PORT` automatically; the app reads it via `server.port=${PORT:8080}`.
- Use Railway internal hostname for `<mariadb-service-host>`.

### 5️⃣ Deploy and verify

- Railway auto-builds using the repository `Dockerfile`.
- After deploy, test:
  - `GET /test`
  - `POST /auth/login`
  - protected case endpoints with auth cookie

### 6️⃣ Recommended production hardening

- Change `DDL_AUTO` from `update` to `validate` after first successful schema creation.
- Restrict `CORS_ALLOWED_ORIGINS` to your exact frontend URL(s).
- Rotate `JWT_SECRET` if exposed.

---

## 🧪 API Endpoints

### 📌 Create Case

**POST** `/cases`

```json
{
  "defendantDetails": "John Doe",
  "crimeType": "Theft",
  "arrestInfo": "Arrested in Delhi",
  "prosecutorDetails": "State Prosecutor",
  "createdBy": 1
}
```

---

### 📌 Test Endpoint

**GET** `/test`

```
Backend working!
```

---

## 🧠 System Design Overview

### Entities:

- User (Registrar, Judge, Lawyer, Admin)
- Case
- Hearing
- Adjournment
- Payment
- CaseAccess

### Relationships:

- Case → multiple Hearings
- Hearing → multiple Adjournments
- User → Payments
- Lawyer → Paid access to closed cases

---

## 🔐 Business Rules

- Only **Registrar** can create cases
- Cases start with **PENDING** status
- Closed cases cannot be modified
- Adjournment requires existing hearing
- Lawyers must **pay before accessing closed cases**

---

## 📌 Future Enhancements

- 🔐 JWT Authentication & Authorization
- 📊 Dashboard & Analytics
- 📅 Court Slot Management
- 💳 Payment Gateway Integration
- 📄 Document Upload System

---

## ⭐ Notes

This project is built as a **scalable backend system** following clean architecture principles and can be extended into a full production-grade judicial platform.

---
