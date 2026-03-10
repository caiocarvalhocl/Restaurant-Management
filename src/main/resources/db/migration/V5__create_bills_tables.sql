-- Bills table
CREATE TABLE bills (
    id BIGSERIAL PRIMARY KEY,
    table_id BIGINT REFERENCES restaurant_tables(id),
    client_name VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    opened_by BIGINT NOT NULL REFERENCES users(id),
    closed_by BIGINT REFERENCES users(id),
    payment_method VARCHAR(20),
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,
    discount DECIMAL(10,2) NOT NULL DEFAULT 0,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    opened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP
);

-- Bill items table
CREATE TABLE bill_items (
    id BIGSERIAL PRIMARY KEY,
    bill_id BIGINT NOT NULL REFERENCES bills(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity DECIMAL(10,3) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    notes VARCHAR(255),
    added_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_bills_status ON bills(status);
CREATE INDEX idx_bills_table ON bills(table_id);
CREATE INDEX idx_bill_items_bill ON bill_items(bill_id);
