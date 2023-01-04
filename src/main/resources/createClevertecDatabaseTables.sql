CREATE TABLE IF NOT EXISTS address (
id SERIAL NOT NULL,
postal_code CHARACTER VARYING(9) NOT NULL,
country CHARACTER VARYING(30) NOT NULL,
city CHARACTER VARYING(30) NOT NULL,
street CHARACTER VARYING(30) NOT NULL,
building_number CHARACTER VARYING(7) NOT NULL,

PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS shop (
id SERIAL NOT NULL,
name CHARACTER VARYING(30) NOT NULL,
address_id INT NOT NULL,
phone CHARACTER VARYING(18) NOT NULL,

PRIMARY KEY(id),

CONSTRAINT fk_address
FOREIGN KEY (address_id)
REFERENCES address (id)
ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS product (
id SERIAL NOT NULL,
description CHARACTER VARYING(60) NOT NULL,
is_auction BOOLEAN NOT NULL,

PRIMARY KEY(id),
UNIQUE(description)
);

CREATE TABLE IF NOT EXISTS storage (
id SERIAL NOT NULL,
product_id INT NOT NULL,
unit CHARACTER VARYING(2) NOT NULL DEFAULT 'шт',
price NUMERIC(11, 2) NOT NULL,
quantity NUMERIC(9, 3) NOT NULL,

PRIMARY KEY(id),

CONSTRAINT fk_product
FOREIGN KEY (product_id)
REFERENCES product (id)
ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS discount_card (
id SERIAL NOT NULL,
number CHARACTER VARYING(9) NOT NULL,
discount_value SMALLINT NOT NULL DEFAULT 0,

PRIMARY KEY(id),
UNIQUE(number)
);
