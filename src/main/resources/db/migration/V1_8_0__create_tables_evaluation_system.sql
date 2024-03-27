create table evaluation_system
(
    id   serial not null
        constraint evaluation_system_pkey
            primary key,
    code varchar(255),
    name varchar(255)
);
