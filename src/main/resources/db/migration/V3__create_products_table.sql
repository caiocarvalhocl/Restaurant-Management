CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    price DECIMAL(10,2) NOT NULL CHECK(price > 0),
    price_type VARCHAR NOT NULL,
    available BOOLEAN DEFAULT TRUE NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

INSERT INTO products (name, description, price, price_type, category_id) VALUES
('Product A', 'Description for Product A', 19.99, 'retail', 1),
('Product B', 'Description for Product B', 29.99, 'wholesale', 2),
('Product C', 'Description for Product C', 9.99, 'retail', 1);
