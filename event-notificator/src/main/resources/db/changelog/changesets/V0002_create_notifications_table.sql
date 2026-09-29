--liquibase formatted sql

--changeset vkoreshkov:V0002_create_notifications_table.sql

CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    is_read boolean,
    created_at timestamp,
    read_at timestamp,
    payload_id BIGINT NOT NULL
);

--rollback DROP TABLE notifications;