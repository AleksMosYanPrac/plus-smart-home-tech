CREATE TABLE IF NOT EXISTS orders (
    order_id UUID DEFAULT gen_random_uuid()  PRIMARY KEY,
    shopping_cart_id UUID NOT NULL,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR(50),
    delivery_weight DOUBLE,
    delivery_volume DOUBLE,
    fragile BOOLEAN,
    total_price DOUBLE,
    delivery_price DOUBLE,
    product_price DOUBLE
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id UUID NOT NULL REFERENCES orders(order_id),
    product_id UUID NOT NULL,
    product_quantity BIGINT NOT NULL
);