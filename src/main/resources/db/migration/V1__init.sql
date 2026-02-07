-- V1__init.sql
-- Purpose: Initial schema creation for lorry management
-- Contains:
--   - lorry table with LR as primary key
-- notes:
--   - No indexes included here (handled in later migrations)
--   - Schema-only migration

CREATE TABLE lorry (
                       lr BIGINT PRIMARY KEY,
    lorry_number VARCHAR(10) NOT NULL,
    date DATE NOT NULL,
    from_location VARCHAR(100),
    to_location VARCHAR(100),
    consignor_name VARCHAR(100) NOT NULL,
    consignor_address TEXT,
    description TEXT,
    weight NUMERIC(10,2) NOT NULL,
    freight NUMERIC(10,2)
);
