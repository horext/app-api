create table academic_period
(
    id        serial not null
        constraint academic_period_pkey
            primary key,
    name      varchar(255),
    from_date timestamp,
    to_date   timestamp,
    code      varchar
);


create table academic_period_organization_unit
(
    id                   serial not null
        constraint academic_period_organization_unit_pkey
            primary key,
    academic_period_id   bigint
        constraint academic_period_organization_unit_academic_period_id_fkey
            references academic_period,
    organization_unit_id bigint
        constraint academic_period_organization_unit_organization_unit_id_fkey
            references organization_unit,
    from_date            timestamp default now(),
    to_date              timestamp
);



create index academic_period_organization_unit_organization_unit_id_idx
    on academic_period_organization_unit using hash (organization_unit_id);