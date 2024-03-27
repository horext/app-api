create table study_plan
(
    id                   serial not null
        constraint study_plan_pkey
            primary key,
    code                 varchar(255),
    from_date            timestamp,
    name                 varchar(255),
    to_date              timestamp,
    organization_unit_id bigint
        constraint study_plan_organization_unit_id_fkey
            references organization_unit,
    created_at           timestamp default now(),
    updated_at           timestamp default now(),
    deleted_at           timestamp
);

create index study_plan_from_date_idx
    on study_plan (from_date);

create index study_plan_organization_unit_id_idx
    on study_plan (organization_unit_id);

create index study_plan_organization_unit_id_idx1
    on study_plan using hash (organization_unit_id);

create index study_plan_to_date_idx
    on study_plan (to_date);