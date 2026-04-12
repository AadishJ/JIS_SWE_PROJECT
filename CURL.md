# JIS Backend API - cURL Guide

Base URL:

```
http://localhost:8080
```

---

# 0. Login / Logout (JWT cookie)

## Login (by name)

Use this if you created users with the sample SQL at the bottom (it doesn't require knowing `userId`).

```bash
curl -i -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Registrar1",
    "password": "pass"
  }'
```

## Login (by userId)

```bash
curl -i -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "password": "pass"
  }'
```

## Who am I?

```bash
curl -i -X GET http://localhost:8080/auth/me \
  --cookie-jar cookies.txt --cookie cookies.txt
```

## Logout

```bash
curl -i -X POST http://localhost:8080/auth/logout \
  --cookie-jar cookies.txt --cookie cookies.txt
```

# 1. Create Case (UC-01)

```bash
curl -X POST http://localhost:8080/cases \
-H "Content-Type: application/json" \
-d '{
  "defendantDetails": "John Doe",
  "crimeType": "Theft",
  "arrestInfo": "Arrested in Delhi",
  "prosecutorDetails": "State Prosecutor",
  "createdBy": 1
}'
```

---

# 2. Edit Case (UC-02)

```bash
curl -X PUT http://localhost:8080/cases/{cin} \
-H "Content-Type: application/json" \
-d '{
  "defendantDetails": "Updated Name",
  "crimeType": "Fraud",
  "arrestInfo": "Updated info",
  "prosecutorDetails": "Updated prosecutor"
}'
```

---

# 3. Schedule Hearing (UC-03)

```bash
curl -X POST http://localhost:8080/cases/hearings \
-H "Content-Type: application/json" \
-d '{
  "cin": "CIN-2026-xxxx",
  "hearingDate": "2026-04-01",
  "courtSlot": "10:00 AM"
}'
```

---

# 4. Record Adjournment (UC-04)

```bash
curl -X POST http://localhost:8080/cases/adjournments \
-H "Content-Type: application/json" \
-d '{
  "hearingId": 1,
  "reason": "Judge unavailable",
  "newHearingDate": "2026-04-10"
}'
```

---

# 5. Close Case (UC-05)

```bash
curl -X POST http://localhost:8080/cases/{cin}/close \
-H "Content-Type: application/json" \
-d '{
  "judgmentSummary": "Defendant found guilty"
}'
```

---

# 6. View Pending Cases (UC-06)

```bash
curl -X GET http://localhost:8080/cases/pending
```

---

# 7. Query Case by CIN

```bash
curl -X GET http://localhost:8080/cases/{cin}
```

---

# 8. Browse Closed Case (UC-07 - Judge/Lawyer)

```bash
curl -X POST http://localhost:8080/cases/access \
-H "Content-Type: application/json" \
-d '{
  "userId": 2,
  "cin": "CIN-2026-xxxx"
}'
```

---

# 9. Test Endpoint

```bash
curl -X GET http://localhost:8080/test
```

---

# Sample User Creation (Manual SQL)

```sql
INSERT INTO users (name, password, role)
VALUES
('Registrar1', 'pass', 'REGISTRAR'),
('Judge1', 'pass', 'JUDGE'),
('Lawyer1', 'pass', 'LAWYER');
```

---

# Notes

- Replace `{cin}` with actual case ID (e.g. `CIN-2026-ab12cd`)
- Ensure user exists before calling APIs
- Lawyer access triggers payment automatically
- Dates must be in `YYYY-MM-DD` format

---

# Tip

Use tools like:

- Postman
- Insomnia
- HTTPie

for easier testing during development.

---
