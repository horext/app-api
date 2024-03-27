create table classroom
(
    id   serial not null
        constraint classroom_pkey
            primary key,
    code varchar(255),
    name varchar(255)
);
