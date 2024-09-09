INSERT INTO categories (category_name, type) VALUES ('Foods', 'expense'),
                                                    ('Health', 'expense'),
                                                    ('House', 'expense'),
                                                    ('Cafe', 'expense'),
                                                    ('Education', 'expense'),
                                                    ('Family', 'expense'),
                                                    ('Gifts', 'expense'),
                                                    ('Sport', 'expense'),
                                                    ('Transport', 'expense'),
                                                    ('Other', 'expense');
INSERT INTO categories (category_name, type) VALUES ('Business', 'income'),
                                                    ('Others', 'income'),
                                                    ('Cash gifts', 'income');
INSERT INTO users (username, password) VALUES ('sharinganyt430@gmail.com', '{noop}123');

INSERT INTO users (username, password)
VALUES
    ('john_doe@gmail.com', 'password123'),
    ('jane_smith@gmail.com', 'securepassword');

INSERT INTO accounts (user_id, account_name, balance, currency)
VALUES
    (1, 'Main', 1500.00, 'USD'),
    (1, 'Kaspi Kz', 3000.00, 'USD'),
    (2, 'Main', 2500.00, 'EUR');
INSERT INTO transactions (account_id, category_id, amount, transaction_date, description)
VALUES
    (5, 1, 500.00, '2024-09-05 10:00:00', 'Payment for groceries'),
    (5, 2, 200.00, '2024-09-04 14:30:00', 'Online purchase');

INSERT INTO account_transfers (from_account_id, to_account_id, amount, currency, description)
VALUES
    (1, 2, 300.00, 'USD', 'Transfer from checking to savings');

INSERT INTO regular_payments (account_id, type, name, category_id, amount, currency, description, start_date, end_date, next_due_date, frequency)
VALUES
    (1, 'expense', 'Monthly Rent', 3, 1200.00, 'USD', 'Rent for apartment', '2024-01-01', '2024-12-31', '2024-09-01', 'monthly'),
    (2, 'income', 'Salary', 4, 2500.00, 'EUR', 'Monthly salary from work', '2024-01-01', '2024-12-31', '2024-09-30', 'monthly');
