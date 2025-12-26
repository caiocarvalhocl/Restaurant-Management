CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO categories (name, description) VALUES
('Appetizers', 'Start your meal with our delicious appetizers.'),
('Main Courses', 'Hearty and satisfying main dishes.'),
('Desserts', 'Sweet treats to finish your meal.')
