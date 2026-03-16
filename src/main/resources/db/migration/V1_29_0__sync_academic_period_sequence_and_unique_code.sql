SELECT setval(
    pg_get_serial_sequence('academic_period', 'id'),
    COALESCE((SELECT MAX(id) FROM academic_period), 1),
    (SELECT MAX(id) IS NOT NULL FROM academic_period)
);

CREATE UNIQUE INDEX IF NOT EXISTS academic_period_code_uindex ON academic_period (code);
