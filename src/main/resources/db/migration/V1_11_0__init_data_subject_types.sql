insert into subject_type (id, code, name, subject_type_parent_id)
values  (1, 'O', 'Obligatorio', null),
        (4, 'E', 'Electivo', null),
        (3, '', 'Electivo de Ingeniería', 4),
        (2, ' ', 'Electivo de Especialidad', 4);
