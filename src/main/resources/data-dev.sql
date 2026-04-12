-- Dev-only seed data.
-- This script is loaded only when running with `--spring.profiles.active=dev`.

-- Users (password is intentionally plain for local dev; login upgrades to BCrypt on first successful login)
INSERT INTO users (name, password, role, created_at)
SELECT 'Registrar1', 'pass', 'REGISTRAR', NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE LOWER(name) = 'registrar1');

INSERT INTO users (name, password, role, created_at)
SELECT 'Judge1', 'pass', 'JUDGE', NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE LOWER(name) = 'judge1');

INSERT INTO users (name, password, role, created_at)
SELECT 'Lawyer1', 'pass', 'LAWYER', NOW()
WHERE NOT EXISTS (SELECT 1 FROM users WHERE LOWER(name) = 'lawyer1');
