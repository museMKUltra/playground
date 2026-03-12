alter table orders
    alter column created_at set default (current_timestamp);

alter table orders
    add constraint orders_users_id_fk
        foreign key (customer_id) references users (id);

