ALTER TABLE subject
ADD CONSTRAINT subject_course_study_plan_unique
UNIQUE (course_id, study_plan_id);