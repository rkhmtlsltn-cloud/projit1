create table customers (
    id serial primary key,
    name varchar(120) not null,
    role varchar(20) not null,
    constraint chk_role check (role in ('ADMIN','MANAGER','CUSTOMER'))
);

create table comics (
    id serial primary key,
    title varchar(120) not null,
    price numeric(10,2) not null,
    stock int not null,
    category varchar(50) not null,
    story text not null default '',
    constraint chk_price check (price > 0),
    constraint chk_stock check (stock >= 0)
);

create table orders (
    id serial primary key,
    customer_id int not null,
    total numeric(10,2) not null,
    constraint fk_orders_customer
        foreign key (customer_id)
        references customers(id),
    constraint chk_total check (total >= 0)
);

create table order_items (
    id serial primary key,
    order_id int not null,
    comic_id int not null,
    quantity int not null,
    price_at_purchase numeric(10,2) not null,
    constraint fk_items_order
        foreign key (order_id)
        references orders(id)
        on delete cascade,
    constraint fk_items_comic
        foreign key (comic_id)
        references comics(id)
        on delete cascade,
    constraint chk_qty check (quantity > 0),
    constraint chk_price_purchase check (price_at_purchase > 0)
);

create table comic_chapters (
    id serial primary key,
    comic_id int not null,
    chapter_number int not null,
    title varchar(200) not null,
    text text not null,
    constraint fk_chapters_comic
        foreign key (comic_id)
        references comics(id)
        on delete cascade,
    constraint unique_chapter_per_comic
        unique (comic_id, chapter_number)
);
