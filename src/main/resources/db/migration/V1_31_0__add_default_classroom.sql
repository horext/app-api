ALTER TABLE classroom
    ADD CONSTRAINT classroom_code_unique UNIQUE (code);

INSERT INTO classroom (code, name)
VALUES ('NO_CLASSROOM', 'Sin aula')
ON CONFLICT (code) DO NOTHING;