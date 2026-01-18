CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID DEFAULT gen_random_uuid()  PRIMARY KEY,
    order_id UUID,
    status VARCHAR(50),
    total_payment NUMERIC(10, 2),
    delivery_total NUMERIC(10, 2),
    fee_total NUMERIC(10, 2)
);