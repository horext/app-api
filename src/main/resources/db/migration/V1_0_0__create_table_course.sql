create table course
(
    id         varchar(255) not null
        constraint course_pkey
            primary key,
    created_at timestamp,
    updated_at timestamp,
    name       varchar(255),
    code  varchar(100)
);
