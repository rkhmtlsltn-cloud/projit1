create table comics (
  id serial primary key,
  title varchar(120),
  price numeric(10,2),
  stock int
);

create table customers (
  id serial primary key,
  name varchar(120)
);

create table orders (
  id serial primary key,
  customer_id int,
  total numeric(10,2)
);

create table order_items (
  id serial primary key,
  order_id int,
  comic_id int,
  quantity int,
  price_at_purchase numeric(10,2)
);

TRUNCATE TABLE order_items, orders, comics, customers RESTART IDENTITY CASCADE;
