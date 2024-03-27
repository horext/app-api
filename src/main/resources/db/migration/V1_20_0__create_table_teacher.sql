create table teacher
(
    code      varchar(255),
    id        serial not null
        constraint teacher_pkey
            primary key,
    full_name varchar
);
