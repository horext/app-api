DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1
                       FROM pg_constraint
                       WHERE conname = 'class_session_pk'
                         AND conrelid = 'class_session'::regclass) THEN
            ALTER TABLE class_session
                ADD CONSTRAINT class_session_pk UNIQUE (schedule_id, day, start_time, end_time, teacher_id,
                                                        classroom_id, class_session_type_id);
        END IF;
    END
$$;
