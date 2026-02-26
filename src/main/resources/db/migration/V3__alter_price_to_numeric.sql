ALTER TABLE current_available_products
ALTER COLUMN price TYPE numeric(19,2)
  USING round(price::numeric, 2);