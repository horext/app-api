ALTER TABLE schedule
    ADD COLUMN course_id varchar(50);

DO
$$
BEGIN
    IF EXISTS (SELECT 1
               FROM schedule_subject ss
                        INNER JOIN subject s ON s.id = ss.subject_id
               GROUP BY ss.schedule_id
               HAVING COUNT(DISTINCT s.course_id) > 1) THEN
        RAISE EXCEPTION 'Cannot assign schedule.course_id: a schedule is linked to subjects from multiple courses';
    END IF;
END
$$;

UPDATE schedule schedule_to_update
SET course_id = schedule_course.course_id
FROM (SELECT ss.schedule_id, MIN(s.course_id) AS course_id
      FROM schedule_subject ss
               INNER JOIN subject s ON s.id = ss.subject_id
      GROUP BY ss.schedule_id) schedule_course
WHERE schedule_to_update.id = schedule_course.schedule_id;

DO
$$
BEGIN
    IF EXISTS (SELECT 1 FROM schedule WHERE course_id IS NULL) THEN
        RAISE EXCEPTION 'Cannot require schedule.course_id: at least one schedule has no linked subject';
    END IF;
END
$$;

ALTER TABLE schedule
    ALTER COLUMN course_id SET NOT NULL,
    ADD CONSTRAINT schedule_course_id_fkey FOREIGN KEY (course_id) REFERENCES course (id);

CREATE INDEX schedule_course_id_idx ON schedule (course_id);
