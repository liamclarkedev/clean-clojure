CREATE TABLE IF NOT EXISTS todo
(
    id           UUID PRIMARY KEY            DEFAULT gen_random_uuid(),
    title        VARCHAR(100)                                          NOT NULL,
    description  VARCHAR(250)                                          NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    completed_at TIMESTAMP WITHOUT TIME ZONE
)