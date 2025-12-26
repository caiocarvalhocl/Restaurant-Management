CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL NOT NULL 
);

INSERT INTO products (name, price) VALUES
('Spring Rolls', 5.99),
('Grilled Chicken', 12.99),
('Cheesecake', 6.49);
