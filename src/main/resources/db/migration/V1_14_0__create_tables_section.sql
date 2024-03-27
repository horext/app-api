create table section
(
    id   varchar(2) not null
        constraint section_pkey
            primary key,
    code varchar(2)
);

create table section_organization_unit
(
    section_id           varchar(255)
        constraint section_organization_unit_section_id_fkey
            references section,
    organization_unit_id bigint
        constraint section_organization_unit_organization_unit_id_fkey
            references organization_unit
);
