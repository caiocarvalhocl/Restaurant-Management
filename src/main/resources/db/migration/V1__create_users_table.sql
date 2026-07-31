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
-- BCrypt hash for 'admin123', generated with Spring Security's BCryptPasswordEncoder (strength 10).
-- To regenerate: new BCryptPasswordEncoder().encode("admin123")
INSERT INTO users (name, email, password, role, active)
VALUES ('Admin', 'admin@restaurant.com', '$2a$10$Rgkfz3mTt4TTKv8iCt4cou0ALKIaeDwbCSVIY8KPqFImg0mhc0JgC', 'OWNER', true);
