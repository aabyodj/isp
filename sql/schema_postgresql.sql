SET client_encoding = 'UTF8';

CREATE TABLE IF NOT EXISTS tariff (
    id SERIAL8 PRIMARY KEY,
    name character varying(15) NOT NULL,
    description character varying(50) NOT NULL,
    price numeric(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS promotions (
    id SERIAL8 PRIMARY KEY,
    name varchar(25) NOT NULL,
    description varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL8 PRIMARY KEY,
    email character varying(25) UNIQUE NOT NULL,
    role_id bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS customer_accounts (
    user_id bigint NOT NULL,
    tariff_id bigint DEFAULT 0,
    balance numeric(10,2) DEFAULT 0,
    permitted_overdraft numeric(10,2) DEFAULT 0,
    payoff_date timestamp without time zone,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO users (email, role_id) VALUES ('admin@example.com', 0);