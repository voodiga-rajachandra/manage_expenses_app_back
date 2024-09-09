-- liquibase formatted sql

-- changeset daurenassanbaev:1
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(128) NOT NULL UNIQUE,
                                     password VARCHAR(128) NOT NULL
);
-- rollback DROP TABLE users;

-- changeset daurenassanbaev:2
CREATE TABLE IF NOT EXISTS accounts (
                                        id SERIAL PRIMARY KEY,
                                        user_id INT REFERENCES users (id) NOT NULL,
                                        account_name VARCHAR(128) NOT NULL,
                                        balance DECIMAL(10, 2) NOT NULL DEFAULT 0.0,
                                        currency VARCHAR(12) NOT NULL CHECK ( currency IN ('USD', 'KZT', 'RUB', 'EUR') )
);
-- rollback DROP TABLE accounts;

-- changeset daurenassanbaev:3
CREATE TABLE IF NOT EXISTS categories (
                                          id SERIAL PRIMARY KEY,
                                          category_name VARCHAR(128) NOT NULL,
                                          type VARCHAR(128) NOT NULL CHECK (type IN ('income', 'expense'))
);
-- rollback DROP TABLE categories;

-- changeset daurenassanbaev:4
CREATE TABLE IF NOT EXISTS transactions (
                                            id SERIAL PRIMARY KEY,
                                            account_id INT REFERENCES accounts (id) NOT NULL,
                                            category_id INT REFERENCES categories (id) NOT NULL,
                                            amount DECIMAL(10, 2) NOT NULL,
                                            transaction_date TIMESTAMP NOT NULL,
                                            description TEXT
);
-- rollback DROP TABLE transactions;

-- changeset daurenassanbaev:5
CREATE TABLE IF NOT EXISTS account_transfers (
                                                 id SERIAL PRIMARY KEY,
                                                 from_account_id INT REFERENCES accounts(id) NOT NULL,
                                                 to_account_id INT REFERENCES accounts(id) NOT NULL,
                                                 amount DECIMAL(10, 2) NOT NULL,
                                                 currency VARCHAR(12) NOT NULL CHECK ( currency IN ('USD', 'KZT', 'RUB', 'EUR') ),
                                                 transfer_date TIMESTAMP NOT NULL DEFAULT NOW(),
                                                 description TEXT
);
-- rollback DROP TABLE account_transfers;

-- changeset daurenassanbaev:6
CREATE TABLE IF NOT EXISTS regular_payments(
                                               id SERIAL PRIMARY KEY,
                                               account_id INT REFERENCES accounts (id) NOT NULL,
                                               type VARCHAR(32) NOT NULL CHECK ( type IN ('income', 'expense') ),
                                               name VARCHAR(128) NOT NULL,
                                               category_id INT REFERENCES categories (id) NOT NULL,
                                               amount DECIMAL(10, 2) NOT NULL,
                                               currency VARCHAR(10) NOT NULL,
                                               description VARCHAR(128),
                                               start_date TIMESTAMP NOT NULL,
                                               end_date TIMESTAMP NOT NULL,
                                               next_due_date TIMESTAMP NOT NULL,
                                               frequency VARCHAR(32) NOT NULL CHECK ((frequency IN ('daily', 'weekly', 'monthly', 'yearly')))
);
-- rollback DROP TABLE regular_payments;

