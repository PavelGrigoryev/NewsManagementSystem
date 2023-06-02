--liquibase formatted sql

--changeset Grigoryev_Pavel:1
CREATE TABLE IF NOT EXISTS users
(
    id           BIGSERIAL PRIMARY KEY,
    firstname    VARCHAR(64)        NOT NULL,
    lastname     VARCHAR(64)        NOT NULL,
    email        VARCHAR(64) UNIQUE NOT NULL,
    password     VARCHAR(64)        NOT NULL,
    created_time TIMESTAMP          NOT NULL,
    updated_time TIMESTAMP          NOT NULL,
    role         VARCHAR(16)        NOT NULL
);
