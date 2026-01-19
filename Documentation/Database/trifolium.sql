CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(25) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password BYTEA,
    provider_id VARCHAR(255),
    provider VARCHAR(6)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    icon VARCHAR(25),
    color CHAR(7),
    user_id BIGINT DEFAULT NULL,
    CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    title VARCHAR(100),
    description TEXT,
    type VARCHAR(7),
    category_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_transaction_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES categories (id)
);