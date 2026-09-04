--liquibase formatted sql

--changeset vkoreshkov:V0002_create_users_table.sql

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(255) NOT NULL,
    pass VARCHAR(500) NOT NULL,
    age INTEGER NOT NULL,
    user_role VARCHAR(10) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_login ON users (login);
--rollback DROP TABLE users;