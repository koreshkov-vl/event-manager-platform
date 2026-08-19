--liquibase formatted sql

--changeset vkoreshkov:V0001_create_locations_table.sql

CREATE TABLE IF NOT EXISTS locations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    capacity INTEGER NOT NULL CHECK (capacity >= 5),
    description TEXT
);

--rollback DROP TABLE locations;