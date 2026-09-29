--liquibase formatted sql

--changeset vkoreshkov:V0001_create_notifications_event_payloads_table.sql

CREATE TABLE IF NOT EXISTS notifications_event_payloads (
    id BIGSERIAL PRIMARY KEY,
    message_id UUID NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    event_id BIGINT NOT NULL,
    occurred_at timestamp NOT NULL,
    owner_id BIGINT,
    changed_by_id BIGINT,
    payload_json jsonb NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_message_id ON notifications_event_payloads (message_id);

--rollback DROP TABLE notifications_event_payloads;