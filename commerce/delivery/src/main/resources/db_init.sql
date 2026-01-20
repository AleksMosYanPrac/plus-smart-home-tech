CREATE TABLE IF NOT EXISTS addresses (
  address_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  country VARCHAR(100),
  city VARCHAR(100),
  street VARCHAR(100),
  house VARCHAR(100),
  flat VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID NOT NULL,
    from_address UUID NOT NULL REFERENCES addresses(address_id),
    to_address UUID NOT NULL REFERENCES addresses(address_id),
    delivery_state VARCHAR(50)
);