CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY NOT NULL,
    quantity BIGINT NOT NULL,
    fragile BOOLEAN,
    weight DOUBLE PRECISION NOT NULL,
    width DOUBLE PRECISION NOT NULL,
    height DOUBLE PRECISION NOT NULL,
    depth DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
  order_booking_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  order_id UUID,
  delivery_id UUID
);

CREATE TABLE IF NOT EXISTS booking_products (
  shopping_cart_id UUID REFERENCES bookings(order_booking_id),
  product_id UUID REFERENCES products(product_id),
  quantity BIGINT
);