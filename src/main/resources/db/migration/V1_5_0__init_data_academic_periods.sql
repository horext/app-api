with
academic_periods (name, code, from_date, to_date) as (
    values
        ('2020-2', '2020-2', null::timestamp, null::timestamp),
        ('2020-1', '2020-1', null::timestamp, null::timestamp),
        ('2019-2', '2019-2', null::timestamp, null::timestamp),
        ('2019-1', '2019-1', null::timestamp, null::timestamp),
        ('2021-1', '2021-1', null::timestamp, null::timestamp),
        ('2021-2', '2021-2', null::timestamp, null::timestamp),
        ('2022-1', '2022-1', null::timestamp, null::timestamp),
        ('2022-2', '2022-2', null::timestamp, null::timestamp),
        ('2023-1', '2023-1', null::timestamp, null::timestamp),
        ('2023-2', '2023-2', null::timestamp, null::timestamp),
        ('2024-1', '2024-1', null::timestamp, null::timestamp)
),
faculty as (
    select id from organization_unit where code = 'I'
),
insert_periods as (
    insert into academic_period (name, code, from_date, to_date)
    select * from academic_periods
    returning id, code
),
academic_period_org_units (code, from_date, to_date) as (
    values
        ('2021-1', '2021-02-19 13:28:57.000000'::timestamp, '2021-07-24 17:36:44.000000'::timestamp),
        ('2021-2', '2021-09-06 12:00:00.000000'::timestamp, '2021-12-31 00:00:00.000000'::timestamp),
        ('2022-1', '2022-04-11 00:00:00.000000'::timestamp, '2022-08-14 00:00:00.000000'::timestamp),
        ('2022-2', '2022-09-19 00:00:00.000000'::timestamp, '2023-02-07 00:00:16.000000'::timestamp),
        ('2023-1', '2023-03-27 06:19:13.206000'::timestamp, '2023-07-21 01:34:05.000000'::timestamp),
        ('2023-2', '2023-08-27 00:16:22.000000'::timestamp, '2023-12-19 00:16:22.000000'::timestamp),
        ('2024-1', '2024-03-18 05:00:00.000000'::timestamp, null::timestamp)
)
insert into academic_period_organization_unit (academic_period_id, organization_unit_id, from_date, to_date)
select p.id, f.id, o.from_date, o.to_date
from academic_period_org_units o
join insert_periods p on p.code = o.code
cross join faculty f;