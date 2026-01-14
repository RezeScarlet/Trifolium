CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(25) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_salt BYTEA,
    password_hash BYTEA,
    provider_id VARCHAR(255),
    provider VARCHAR(6)
);

CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    icon VARCHAR(25),
    user_id INTEGER DEFAULT NULL,
    CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    title VARCHAR(100),
    description TEXT,
    type VARCHAR(7),
    category_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES categories (id)
);