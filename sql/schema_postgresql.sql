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
    email character varying(25) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS customers (
    user_id bigint NOT NULL,
    tariff_id bigint DEFAULT 0,
    balance numeric(10,2) DEFAULT 0,
    permitted_overdraft numeric(10,2) DEFAULT 0,
    payoff_date timestamp,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS employees (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);