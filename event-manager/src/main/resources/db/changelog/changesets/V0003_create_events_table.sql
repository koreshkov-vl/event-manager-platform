--liquibase formatted sql

--changeset vkoreshkov:V0003_create_events_table.sql

CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_at timestamp NOT NULL,
    duration_minutes INTEGER NOT NULL,
    max_places INTEGER NOT NULL,
    occupied_places INTEGER DEFAULT 0 NOT NULL,
    cost INTEGER NOT NULL,
    status VARCHAR(255) NOT NULL,
    location_id BIGINT REFERENCES locations(id) ON DELETE SET NULL,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_events_location_id ON events(location_id);
CREATE INDEX IF NOT EXISTS idx_events_user_id ON events(user_id);
--rollback DROP TABLE events;
