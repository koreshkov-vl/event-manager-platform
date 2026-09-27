--liquibase formatted sql

--changeset vkoreshkov:V0003_create_notifications_index.sql

CREATE INDEX idx_notifications_read_at
    ON notifications (read_at)
    WHERE is_read = true;

--rollback DROP INDEX idx_notifications_read_at;