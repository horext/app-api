insert into academic_period (id, name, code, from_date, to_date)
values  (1, '2020-2', '2020-2', null, null),
        (2, '2020-1', '2020-1', null, null),
        (3, '2019-2', '2019-2', null, null),
        (4, '2019-1', '2019-1', null, null),
        (5, '2021-1', '2021-1', null, null),
        (6, '2021-2', '2021-2', null, null),
        (7, '2022-1', '2022-1', null, null),
        (8, '2022-2', '2022-2', null, null),
        (9, '2023-1', '2023-1', null, null),
        (10, '2023-2', '2023-2', null, null),
        (11, '2024-1', '2024-1', null, null);

insert into academic_period_organization_unit (id, academic_period_id, organization_unit_id, from_date, to_date)
values  (1, 5, 31, '2021-02-19 13:28:57.000000', '2021-07-24 17:36:44.000000'),
        (2, 6, 31, '2021-09-06 12:00:00.000000', '2021-12-31 00:00:00.000000'),
        (3, 7, 31, '2022-04-11 00:00:00.000000', '2022-08-14 00:00:00.000000'),
        (5, 8, 31, '2022-09-19 00:00:00.000000', '2023-02-07 00:00:16.000000'),
        (6, 9, 31, '2023-03-27 06:19:13.206000', '2023-07-21 01:34:05.000000'),
        (7, 10, 31, '2023-08-27 00:16:22.000000', '2023-12-19 00:16:22.000000'),
        (8, 11, 31, '2024-03-18 05:00:00.000000', null);