--liquibase formatted sql

--changeset vkoreshkov:V0005_create_outbox_messages_table.sql

CREATE TABLE outbox_messages (
    id           BIGSERIAL    PRIMARY KEY,
    message_id   UUID         NOT NULL UNIQUE,
    topic        VARCHAR(255) NOT NULL,
    message_key  VARCHAR(255) NOT NULL,
    payload      TEXT         NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL,
    processed_at TIMESTAMPTZ,
    attempts     INT          NOT NULL DEFAULT 0,
    last_error   TEXT
);

CREATE INDEX idx_outbox_unprocessed
    ON outbox_messages (created_at)
    WHERE processed_at IS NULL;

--rollback DROP TABLE outbox_messages;
