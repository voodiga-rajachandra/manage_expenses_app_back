--liquibase formatted sql

--changeset daurenassanbaev:1
CREATE TABLE IF NOT EXISTS revision
(
    id SERIAL PRIMARY KEY ,
    timestamp BIGINT NOT NULL
);

--changeset daurenassanbaev:2
CREATE TABLE IF NOT EXISTS transactions_aud
(
    id BIGINT,
    rev INT REFERENCES revision (id),
    revtype SMALLINT ,
    account_id INT REFERENCES accounts (id) NOT NULL,
    category_id INT REFERENCES categories (id) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    description TEXT

);
