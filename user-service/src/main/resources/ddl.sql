CREATE TABLE IF NOT EXISTS users
(
    id           BIGSERIAL PRIMARY KEY,
    firstname    varchar(64)        NOT NULL,
    lastname     varchar(64)        NOT NULL,
    email        varchar(64) UNIQUE NOT NULL,
    password     varchar(64)        NOT NULL,
    created_time TIMESTAMP          NOT NULL,
    updated_time TIMESTAMP          NOT NULL,
    role         varchar(16)        NOT NULL
);
