update study_plan
set created_at = coalesce(created_at, now()),
    updated_at = coalesce(updated_at, now());

alter table study_plan
    alter column created_at set default now(),
    alter column created_at set not null,
    alter column updated_at set default now(),
    alter column updated_at set not null;
