SET client_encoding = 'UTF8';

CREATE TABLE IF NOT EXISTS "tariffs" (
    "id" SERIAL8 PRIMARY KEY,
    "name" character varying(15) NOT NULL,
    "description" character varying(50) NOT NULL,
    "bandwidth" int NOT NULL,
    "included_traffic" bigint NOT NULL,
    "price" numeric(10,2) NOT NULL,
    "active" boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS "promotions" (
    "id" SERIAL8 PRIMARY KEY,
    "name" varchar(25) NOT NULL,
    "description" varchar(100) NOT NULL,
    "active_since" TIMESTAMP NOT NULL,
    "active_until" TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS "users" (
    "id" SERIAL8 PRIMARY KEY,
    "email" character varying(50) UNIQUE NOT NULL,
    "password_hash" bytea NOT NULL,
    "active" boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS "customers" (
    "user_id" bigint PRIMARY KEY,
    "balance" numeric(10,2) NOT NULL,
    "permitted_overdraft" numeric(10,2) NOT NULL,
    "payoff_date" timestamp NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "users"("id")
);

CREATE TABLE IF NOT EXISTS "employees" (
    "user_id" bigint PRIMARY KEY,
    "role_id" int NOT NULL,
    FOREIGN KEY ("user_id") REFERENCES "users"("id")
);

CREATE TABLE IF NOT EXISTS "subscriptions" (
    "id" SERIAL8 PRIMARY KEY,
    "customer_id" bigint NOT NULL,
    "tariff_id" bigint NOT NULL,
    "price" numeric(10, 2) NOT NULL,
    "traffic_consumed" bigint NOT NULL,
    "traffic_per_period" bigint NOT NULL,
    "active_since" TIMESTAMP NOT NULL,
    "active_until" TIMESTAMP NOT NULL,
    FOREIGN KEY ("customer_id") REFERENCES "customers"("user_id"),
    FOREIGN KEY ("tariff_id") REFERENCES "tariffs"("id")
);