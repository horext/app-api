
create table organization_unit_type
(
    id   serial not null
        constraint organization_unit_type_pkey
            primary key,
    name varchar(255)
);


create table organization_unit
(
    id                        serial not null
        constraint organization_unit_pkey
            primary key,
    code                      varchar(255),
    name                      varchar(255),
    organization_unit_type_id bigint
        constraint organization_unit_organization_unit_type_id_fkey
            references organization_unit_type,
    parent_organization_id    bigint
        constraint organization_unit_parent_organization_id_fkey
            references organization_unit
);
