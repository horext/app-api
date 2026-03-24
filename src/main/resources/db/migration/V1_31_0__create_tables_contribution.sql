CREATE TABLE IF NOT EXISTS contribution (
    id BIGSERIAL PRIMARY KEY,
    author_name VARCHAR(255) NOT NULL,
    author_email VARCHAR(255),
    committed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT contribution_unq UNIQUE (author_name, author_email)
);

CREATE TABLE IF NOT EXISTS hourly_load_contribution (
    hourly_load_id BIGINT NOT NULL REFERENCES hourly_load(id) ON DELETE CASCADE,
    contribution_id BIGINT NOT NULL REFERENCES contribution(id) ON DELETE CASCADE,
    PRIMARY KEY (hourly_load_id, contribution_id)
);

CREATE TABLE IF NOT EXISTS study_plan_contribution (
    study_plan_id BIGINT NOT NULL REFERENCES study_plan(id) ON DELETE CASCADE,
    contribution_id BIGINT NOT NULL REFERENCES contribution(id) ON DELETE CASCADE,
    PRIMARY KEY (study_plan_id, contribution_id)
);
