-- liquibase formatted sql

-- changeset daurenassanbaev:1
ALTER TABLE transactions ADD COLUMN image VARCHAR(64);

ALTER TABLE transactions_aud ADD COLUMN image VARCHAR(64);