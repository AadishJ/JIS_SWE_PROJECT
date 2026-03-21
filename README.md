# 🏛️ Judicial Information System (JIS)

A **Spring Boot + MySQL backend system** for managing judicial case workflows including case creation, hearings, adjournments, and access control.

---

## 🚀 Features

* ⚖️ Case Management (Create, Update, Close)
* 📅 Hearing Scheduling & Adjournments
* 💳 Lawyer Payment System for Case Access
* 🔍 Query Cases (by CIN, date, status)
* 👤 Role-Based Access (Registrar, Judge, Lawyer, Admin)
* 📚 Access Control for Closed Cases

---

## 🧱 Tech Stack

* **Backend:** Spring Boot (Java)
* **Database:** MySQL / MariaDB
* **ORM:** Spring Data JPA (Hibernate)
* **Build Tool:** Maven
* **Utilities:** Lombok

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

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jis_db
spring.datasource.username=jis_user
spring.datasource.password=password123

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

---

### 3️⃣ Run Application

```bash
mvn spring-boot:run
```

Server will start at:

```
http://localhost:8080
```

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

* User (Registrar, Judge, Lawyer, Admin)
* Case
* Hearing
* Adjournment
* Payment
* CaseAccess

### Relationships:

* Case → multiple Hearings
* Hearing → multiple Adjournments
* User → Payments
* Lawyer → Paid access to closed cases

---

## 🔐 Business Rules

* Only **Registrar** can create cases
* Cases start with **PENDING** status
* Closed cases cannot be modified
* Adjournment requires existing hearing
* Lawyers must **pay before accessing closed cases**

---

## 📌 Future Enhancements

* 🔐 JWT Authentication & Authorization
* 📊 Dashboard & Analytics
* 📅 Court Slot Management
* 💳 Payment Gateway Integration
* 📄 Document Upload System

---

## ⭐ Notes

This project is built as a **scalable backend system** following clean architecture principles and can be extended into a full production-grade judicial platform.

---
