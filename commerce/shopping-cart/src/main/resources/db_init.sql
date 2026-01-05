CREATE TABLE IF NOT EXISTS carts (
    shopping_cart_id UUID DEFAULT random_uuid()  PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    is_active BOOLEAN
);

CREATE TABLE IF NOT EXISTS cart_products (
    cart_id UUID NOT NULL REFERENCES carts(shopping_cart_id),
    product_id UUID NOT NULL,
    product_quantity BIGINT NOT NULL
);