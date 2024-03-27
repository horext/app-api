
create table subject_type
(
    id                     serial not null
        constraint subject_type_pkey
            primary key,
    code                   varchar(255),
    name                   varchar(255),
    subject_type_parent_id bigint
        constraint subject_type_subject_type_parent_id_fkey
            references subject_type
);


create table subject
(
    id                               serial not null
        constraint subject_pkey
            primary key,
    credits                          integer,
    cycle                            integer,
    max_cycle                        integer,
    min_cycle                        integer,
    note                             varchar(255),
    required_credits                 integer,
    total_weekly_hours               integer,
    weekly_laboratory_hours          integer,
    weekly_practice_hours            integer,
    weekly_practice_laboratory_hours integer,
    weekly_theory_hours              integer,
    course_id                        varchar(255)
        constraint subject_course_id_fkey
            references course,
    evaluation_system_id             bigint
        constraint subject_evaluation_system_id_fkey
            references evaluation_system,
    study_plan_id                    bigint
        constraint subject_study_plan_id_fkey
            references study_plan,
    subject_type_id                  bigint
        constraint subject_subject_type_id_fkey
            references subject_type
);