-- V1: Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for email lookups
CREATE INDEX idx_users_email ON users(email);

-- Insert default admin user (password: admin123)
-- BCrypt hash for 'admin123'
INSERT INTO users (name, email, password, role, active)
VALUES ('Admin', 'admin@restaurant.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqQb9i9mYGVcvCh.L9Xj9u.L9Xj9u', 'OWNER', true);
