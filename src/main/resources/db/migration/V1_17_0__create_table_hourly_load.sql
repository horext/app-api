
create table hourly_load
(
    id                                   serial not null
        constraint hourly_load_pkey
            primary key,
    created_at                           timestamp default now(),
    updated_at                           timestamp default now(),
    published_at                         timestamp,
    academic_period_organization_unit_id bigint
        constraint hourly_load_academic_period_organization_unit_id_fkey
            references academic_period_organization_unit,
    published                            boolean,
    checked_at                           timestamp,
    name                                 varchar
);


create unique index hourly_load_academic_period_organization_unit_id_uindex
    on hourly_load (academic_period_organization_unit_id);

create index hourly_load_published_at_idx
    on hourly_load (published_at desc);