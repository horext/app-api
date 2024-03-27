create table schedule_subject
(
    id             serial not null
        constraint schedule_subject_pkey
            primary key,
    from_datetime  timestamp,
    to_datetime    timestamp,
    schedule_id    bigint
        constraint schedule_subject_schedule_id_fkey
            references schedule,
    subject_id     bigint
        constraint schedule_subject_subject_id_fkey
            references subject,
    hourly_load_id bigint
        constraint schedule_subject_hourly_load_id_fkey
            references hourly_load,
    constraint schedule_subject_hourly_load_id_schedule_id_subject_id_key
        unique (hourly_load_id, schedule_id, subject_id)
);



create unique index schedule_subject_hourly_load_id_schedule_id_subject_id_uindex
    on schedule_subject (hourly_load_id, schedule_id, subject_id);

create index schedule_subject_hourly_load_id_subject_id_idx
    on schedule_subject (hourly_load_id, subject_id);

create index schedule_subject_schedule_id_idx
    on schedule_subject (schedule_id);
