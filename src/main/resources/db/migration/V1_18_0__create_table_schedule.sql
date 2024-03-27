create table schedule
(
    id         serial not null
        constraint schedule_pkey
            primary key,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    vacancies  integer,
    section_id varchar(255)
        constraint schedule_section_id_fkey
            references section,
    delete_at  timestamp,
    deleted_at timestamp
);

create index schedule_delete_at_index
    on schedule (delete_at);

create index schedule_section_id_idx
    on schedule using hash (section_id);
