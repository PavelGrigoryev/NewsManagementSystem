--liquibase formatted sql

--changeset Grigoryev_Pavel:1
CREATE TABLE IF NOT EXISTS news
(
    id    BIGSERIAL PRIMARY KEY,
    time  TIMESTAMP    NOT NULL,
    title VARCHAR(255) NOT NULL,
    text  VARCHAR      NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id       BIGSERIAL PRIMARY KEY,
    time     TIMESTAMP   NOT NULL,
    text     VARCHAR     NOT NULL,
    username VARCHAR(64) NOT NULL,
    news_id  BIGINT REFERENCES news (id)
);
