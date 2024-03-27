create table class_session_type
(
    id   serial not null
        constraint class_session_type_pkey
            primary key,
    code varchar(255),
    name varchar(255)
);

create table class_session
(
    id                    serial not null
    constraint class_session_pkey
    primary key,
    created_at            timestamp default now(),
    updated_at            timestamp default now(),
    day                   integer,
    deleted_at            timestamp,
    end_time              time,
    start_time            time,
    class_session_type_id bigint
    constraint class_session_class_session_type_id_fkey
    references class_session_type,
    classroom_id          bigint
    constraint class_session_classroom_id_fkey
    references classroom,
    schedule_id           bigint
    constraint class_session_schedule_id_fkey
    references schedule,
    teacher_id            bigint
    constraint class_session_teacher_id_fkey
    references teacher,
    constraint class_session_pk
    unique (schedule_id, day, start_time, end_time, teacher_id, classroom_id, class_session_type_id)
);


create index class_session_class_session_type_id_index
    on class_session (class_session_type_id);

create index class_session_classroom_id_index
    on class_session (classroom_id);

create index class_session_created_at_index
    on class_session (created_at);

create index class_session_schedule_id_index
    on class_session (schedule_id);

create index class_session_teacher_id_index
    on class_session (teacher_id);