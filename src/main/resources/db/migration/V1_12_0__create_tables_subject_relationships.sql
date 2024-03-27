create table subject_relationship_type
(
    id   serial not null
        constraint subject_relationship_type_pkey
            primary key,
    name varchar(255)
);


create table subject_relationship
(
    id                           serial not null
        constraint subject_relationship_pkey
            primary key,
    from_subject_id              bigint
        constraint subject_relationship_from_subject_id_fkey
            references subject,
    subject_relationship_type_id bigint
        constraint subject_relationship_subject_relationship_type_id_fkey
            references subject_relationship_type,
    to_subject_id                bigint
        constraint subject_relationship_to_subject_id_fkey
            references subject,
    created_at                   timestamp default now(),
    updated_at                   timestamp default now(),
);
