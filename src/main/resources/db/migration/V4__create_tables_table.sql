CREATE TABLE restaurant_tables (
    id BIGSERIAL PRIMARY KEY,
    table_number INTEGER NOT NULL UNIQUE,
    capacity INTEGER NOT NULL DEFAULT 4,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL );

-- Insert some seed data
INSERT INTO restaurant_tables (table_number, capacity, status) VALUES
(1, 4, 'AVAILABLE'),
(2, 4, 'AVAILABLE'),
(3, 6, 'AVAILABLE'),
(4, 2, 'AVAILABLE'),
(5, 8, 'AVAILABLE');
