-- liquibase formatted sql

-- changeset daurenassanbaev:1
ALTER TABLE users ADD COLUMN created_at TIMESTAMP;
ALTER TABLE users ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE accounts ADD COLUMN created_at TIMESTAMP;
ALTER TABLE accounts ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE account_transfers ADD COLUMN created_at TIMESTAMP;
ALTER TABLE account_transfers ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE categories ADD COLUMN created_at TIMESTAMP;
ALTER TABLE categories ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE regular_payments ADD COLUMN created_at TIMESTAMP;
ALTER TABLE regular_payments ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE transactions ADD COLUMN created_at TIMESTAMP;
ALTER TABLE transactions ADD COLUMN modified_at TIMESTAMP;



ALTER TABLE users ADD COLUMN created_by VARCHAR(32);
ALTER TABLE users ADD COLUMN modified_by VARCHAR(32);

ALTER TABLE account_transfers ADD COLUMN created_by VARCHAR(32);
ALTER TABLE account_transfers ADD COLUMN modified_by VARCHAR(32);

ALTER TABLE accounts ADD COLUMN created_by VARCHAR(32);
ALTER TABLE accounts ADD COLUMN modified_by VARCHAR(32);

ALTER TABLE transactions ADD COLUMN created_by VARCHAR(32);
ALTER TABLE transactions ADD COLUMN modified_by VARCHAR(32);

ALTER TABLE regular_payments ADD COLUMN created_by VARCHAR(32);
ALTER TABLE regular_payments ADD COLUMN modified_by VARCHAR(32);

ALTER TABLE categories ADD COLUMN created_by VARCHAR(32);
ALTER TABLE categories ADD COLUMN modified_by VARCHAR(32);
