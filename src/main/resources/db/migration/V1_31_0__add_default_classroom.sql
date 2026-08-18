INSERT INTO classroom (code, name)
VALUES ('NO_CLASSROOM', 'Sin aula')
ON CONFLICT (code) DO NOTHING;