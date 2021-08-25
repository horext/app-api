create sequence hibernate_sequence;


create table academic_period
(
    id serial not null
constraint academic_period_pkey
primary key,
    name varchar(255),
    from_date timestamp,
    to_date timestamp,
    code varchar
);


create table access_link
(
    uuid uuid not null
constraint access_link_pkey
primary key,
    created_at timestamp,
    updated_at timestamp
);


create table class_session_type
(
    id serial not null
constraint class_session_type_pkey
primary key,
    code varchar(255),
    name varchar(255)
);


create table course
(
    id varchar(255) not null
constraint course_pkey
primary key,
    created_at timestamp,
    updated_at timestamp,
    name varchar(255)
);


create table data
(
    course_code varchar,
    name_course varchar,
    section varchar,
    system_evaluation varchar,
    teacher varchar,
    type_class_session varchar,
    classroom varchar,
    day varchar,
    start_time varchar,
    end_time varchar,
    dni varchar,
    vacancies integer
);



create table evaluation_system
(
    id serial not null
        constraint evaluation_system_pkey
            primary key,
    code varchar(255),
    name varchar(255)
);


create table organization_unit_type
(
    id serial not null
constraint organization_unit_type_pkey
primary key,
    name varchar(255)
);


create table organization_unit
(
    id serial not null
constraint organization_unit_pkey
primary key,
    code varchar(255),
    name varchar(255),
    organization_unit_type_id bigint
constraint organization_unit_organization_unit_type_id_fkey
references organization_unit_type,
    parent_organization_id bigint
constraint organization_unit_parent_organization_id_fkey
references organization_unit
);



create table academic_period_organization_unit
(
    id serial not null
constraint academic_period_organization_unit_pkey
primary key,
    academic_period_id bigint
constraint academic_period_organization_unit_academic_period_id_fkey
references academic_period,
    organization_unit_id bigint
constraint academic_period_organization_unit_organization_unit_id_fkey
references organization_unit,
    from_date timestamp default now(),
    to_date timestamp
);



create index academic_period_organization_unit_organization_unit_id_idx
on academic_period_organization_unit using hash (organization_unit_id);

create table hourly_load
(
    id serial not null
constraint hourly_load_pkey
primary key,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    published_at timestamp,
    academic_period_organization_unit_id bigint
constraint hourly_load_academic_period_organization_unit_id_fkey
references academic_period_organization_unit,
    published boolean,
    checked_at timestamp,
    name varchar
);


create unique index hourly_load_academic_period_organization_unit_id_uindex
on hourly_load (academic_period_organization_unit_id);

create index hourly_load_published_at_idx
on hourly_load (published_at desc);

create table person
(
    id serial not null
constraint person_pkey
primary key,
    first_name varchar(255),
    last_name varchar(255) default ''::character varying
);



create table section
(
    id varchar(2) not null
constraint section_pkey
primary key,
    code varchar(2)
);



create table classroom
(
    id serial not null
constraint classroom_pkey
primary key,
    code varchar(255),
    name varchar(255)
);


create table schedule
(
    id serial not null
constraint schedule_pkey
primary key,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    vacancies integer,
    section_id varchar(255)
constraint schedule_section_id_fkey
references section,
    delete_at timestamp,
    deleted_at timestamp
);



create index schedule_delete_at_index
on schedule (delete_at);

create index schedule_section_id_idx
on schedule using hash (section_id);

create table section_organization_unit
(
    section_id varchar(255)
constraint section_organization_unit_section_id_fkey
references section,
    organization_unit_id bigint
constraint section_organization_unit_organization_unit_id_fkey
references organization_unit
);



create table study_plan
(
    id serial not null
constraint study_plan_pkey
primary key,
    code varchar(255),
    from_date timestamp,
    name varchar(255),
    to_date timestamp,
    organization_unit_id bigint
constraint study_plan_organization_unit_id_fkey
references organization_unit,
    speciality_id bigint
);



create index study_plan_from_date_idx
on study_plan (from_date);

create index study_plan_organization_unit_id_idx
on study_plan (organization_unit_id);

create index study_plan_organization_unit_id_idx1
on study_plan using hash (organization_unit_id);

create index study_plan_to_date_idx
on study_plan (to_date);

create table subject_relationship_type
(
    id serial not null
constraint subject_relationship_type_pkey
primary key,
    name varchar(255)
);


create table subject_type
(
    id serial not null
constraint subject_type_pkey
primary key,
    code varchar(255),
    name varchar(255),
    subject_type_parent_id bigint
constraint subject_type_subject_type_parent_id_fkey
references subject_type
);



create table teacher
(
    code varchar(255),
    id serial not null
constraint teacher_pkey
primary key
constraint teacher_id_fkey
references person,
    full_name varchar
);



create table class_session
(
    id serial not null
constraint class_session_pkey
primary key,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    day integer,
    deleted_at timestamp,
    end_time time,
    start_time time,
    class_session_type_id bigint
constraint class_session_class_session_type_id_fkey
references class_session_type,
    classroom_id bigint
constraint class_session_classroom_id_fkey
references classroom,
    schedule_id bigint
constraint class_session_schedule_id_fkey
references schedule,
    teacher_id bigint
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

create table organization_unit_teacher
(
    id serial not null
constraint organization_unit_teacher_pkey
primary key,
    from_datetime timestamp,
    to_datetime timestamp,
    organization_unit_id bigint
constraint organization_unit_teacher_organization_unit_id_fkey
references organization_unit,
    teacher_id bigint
constraint organization_unit_teacher_teacher_id_fkey
references teacher
);



create table subject
(
    id serial not null
constraint subject_pkey
primary key,
    credits integer,
    cycle integer,
    max_cycle integer,
    min_cycle integer,
    note varchar(255),
    required_credits integer,
    total_weekly_hours integer,
    weekly_laboratory_hours integer,
    weekly_practice_hours integer,
    weekly_practice_laboratory_hours integer,
    weekly_theory_hours integer,
    course_id varchar(255)
constraint subject_course_id_fkey
references course,
    evaluation_system_id bigint
constraint subject_evaluation_system_id_fkey
references evaluation_system,
    study_plan_id bigint
constraint subject_study_plan_id_fkey
references study_plan,
    subject_type_id bigint
constraint subject_subject_type_id_fkey
references subject_type
);



create index subject_evaluation_system_id_idx
on subject (evaluation_system_id);

create index subject_study_plan_id_idx
on subject using hash (study_plan_id);

create index subject_subject_type_id_idx
on subject (subject_type_id);

create table schedule_subject
(
    id serial not null
constraint schedule_subject_pkey
primary key,
    from_datetime timestamp,
    to_datetime timestamp,
    schedule_id bigint
constraint schedule_subject_schedule_id_fkey
references schedule,
    subject_id bigint
constraint schedule_subject_subject_id_fkey
references subject,
    hourly_load_id bigint
constraint schedule_subject_hourly_load_id_fkey
references hourly_load,
    constraint schedule_subject_hourly_load_id_schedule_id_subject_id_key
unique (hourly_load_id, schedule_id, subject_id),
    constraint ukbqj658kxocjwnvcmx66y6yo30
unique (hourly_load_id, schedule_id, subject_id)
);



create unique index schedule_subject_hourly_load_id_schedule_id_subject_id_uindex
on schedule_subject (hourly_load_id, schedule_id, subject_id);

create index schedule_subject_hourly_load_id_subject_id_idx
on schedule_subject (hourly_load_id, subject_id);

create index schedule_subject_schedule_id_idx
on schedule_subject (schedule_id);

create table access_link_schedule_subject
(
    access_link_id uuid
constraint access_link_schedule_subject_access_link_id_fkey
references access_link,
    schedule_subject_id bigint
constraint access_link_schedule_subject_schedule_subject_id_fkey
references schedule_subject
);



create table schedule_subject_access_links
(
    schedule_subject_id bigint
constraint schedule_subject_access_links_schedule_subject_id_fkey
references schedule_subject,
    access_links_uuid uuid
constraint schedule_subject_access_links_access_links_uuid_fkey
references access_link
);



create table subject_relationship
(
    id serial not null
constraint subject_relationship_pkey
primary key,
    from_subject_id bigint
constraint subject_relationship_from_subject_id_fkey
references subject,
    subject_relationship_type_id bigint
constraint subject_relationship_subject_relationship_type_id_fkey
references subject_relationship_type,
    to_subject_id bigint
constraint subject_relationship_to_subject_id_fkey
references subject
);



create procedure generate_hourly_load(faculty_id bigint)
language plpgsql
as $$
DECLARE
RESUME         RECORD;
R              RECORD;
R1             RECORD;
R2             RECORD;
hourly_load_in bigint;
updated_at_in  timestamp;
last_update  timestamp;
schedule_in    bigint;
day_number     bigint;
begin
select max(updated_at) from batch.schedule_resume
where codigo_facultad =  (select ou.code from organization_unit ou where ou.id = faculty_id)
and deleted_at is null
into last_update;

insert into hourly_load(academic_period_organization_unit_id, updated_at)
values ((select id
from academic_period_organization_unit
where organization_unit_id = faculty_id
-- and from_date > now()
and to_date is null
), last_update-'23:00:00'::time)
ON CONFLICT (academic_period_organization_unit_id)
DO UPDATE SET checked_at=now()
returning id,updated_at into hourly_load_in, updated_at_in;

raise info 'hourly load id  % , updated at %',hourly_load_in,updated_at_in;
FOR RESUME IN
SELECT distinct (sr.curso, sr.seccion),
    sr.curso   course_code,
    sr.seccion section,
    sr.vacantes vacancies
FROM batch.schedule_resume sr
where sr.updated_at > updated_at_in
and sr.codigo_facultad = (select ou.code from organization_unit ou where ou.id = faculty_id)
and deleted_at is null
LOOP

raise info 'Curso % , Seccion %',RESUME.course_code,RESUME.section;
-- Para cada curso-seccion
if (not exists(select s2.id
from schedule_subject ss
inner join schedule s2 on s2.id = ss.schedule_id
inner join subject s3 on ss.subject_id = s3.id
inner join hourly_load hl on hl.id = ss.hourly_load_id
where s2.section_id = RESUME.section
and s3.course_id = RESUME.course_code
and hourly_load_id = hourly_load_in
)) then
raise info 'Agregando...';
INSERT INTO schedule (section_id, vacancies)
values (RESUME.section, RESUME.vacancies)
returning id into schedule_in;
FOR R1 IN
select s.id subject_id
from subject s
inner join study_plan sp
on sp.id = s.study_plan_id
inner join organization_unit ou on ou.id = sp.organization_unit_id
where course_id = RESUME.course_code
and ou.parent_organization_id = faculty_id
LOOP
insert into schedule_subject(schedule_id, subject_id, hourly_load_id)
values (schedule_in, R1.subject_id, hourly_load_in);
end loop;
FOR R IN
SELECT inicio   start_time,
    fin      end_time,
    aula     classroom,
    dni      dni,
    vacantes vacancies,
    tipo     type_class_session,
    dia      day_week
FROM batch.schedule_resume
where curso = RESUME.course_code
and seccion = RESUME.section
and codigo_facultad = (select code from organization_unit where id = faculty_id)
and  updated_at > updated_at_in
and  deleted_at is null
LOOP
case R.day_week
when 'Lunes' then day_number = 1;
when 'Martes' then day_number = 2;
when 'Miercoles' then day_number = 3;
when 'Jueves' then day_number = 4;
when 'Viernes' then day_number = 5;
when 'Sábado' then day_number = 6;
when 'Domingo' then day_number = 0;
else day_number = null;
end case;
INSERT INTO class_session
(day, end_time, start_time,
    class_session_type_id,
    classroom_id,
    schedule_id, teacher_id)
values (day_number, R.end_time::time,
    R.start_time::time,
    (select id
from class_session_type
where code = R.type_class_session
limit 1
),
(select id
from classroom
where code = R.classroom
limit 1
),
schedule_in,
    (select id
from teacher
where code = R.dni
limit 1
));
END LOOP;
else

raise info 'Actualizando...';
for R2 in (select distinct (schedule_id) , schedule_id
from schedule_subject ss
inner join schedule s2 on s2.id = ss.schedule_id
inner join subject s3 on ss.subject_id = s3.id
inner join hourly_load hl on hl.id = ss.hourly_load_id
where s2.section_id = RESUME.section
and s3.course_id = RESUME.course_code
and ss.hourly_load_id = hourly_load_in)
loop
update  class_session set deleted_at = now()
where schedule_id = R2.schedule_id
and deleted_at is null;
FOR R IN
SELECT inicio   start_time,
    fin      end_time,
    aula     classroom,
    dni      dni,
    vacantes vacancies,
    tipo     type_class_session,
    dia      day_week
FROM batch.schedule_resume sr
where sr.curso = RESUME.course_code
and sr.seccion = RESUME.section
and sr.updated_at > updated_at_in
and sr.codigo_facultad = (select code from organization_unit where id = faculty_id)
and deleted_at is null
LOOP
case R.day_week
when 'Lunes' then day_number = 1;
when 'Martes' then day_number = 2;
when 'Miercoles' then day_number = 3;
when 'Jueves' then day_number = 4;
when 'Viernes' then day_number = 5;
when 'Sábado' then day_number = 6;
when 'Domingo' then day_number = 0;
else day_number = null;
end case;
INSERT INTO class_session
(day, end_time, start_time,
    class_session_type_id,
    classroom_id,
    schedule_id, teacher_id)
values (day_number, R.end_time::time,
    R.start_time ::time,
    (select id
from class_session_type
where code = R.type_class_session
limit 1
),
(select id
from classroom
where code = R.classroom
limit 1
),
R2.schedule_id,
    (select id
from teacher
where code = R.dni
limit 1
))
ON CONFLICT on constraint class_session_pk
DO
UPDATE SET deleted_at = null;

END LOOP;
update schedule set updated_at =
    (select GREATEST(max(updated_at), max(deleted_at) )from class_session where schedule_id = R2.schedule_id)
where id= R2.schedule_id;
end loop;

end if;
END LOOP;

update hourly_load  set updated_at = last_update where id= hourly_load_in;
end ;
$$;


create extension unaccent;