--liquibase formatted sql

--changeset rntgroup:1
CREATE TABLE IF NOT EXISTS department
(
    id                   SERIAL PRIMARY KEY,
    name                 TEXT NOT NULL UNIQUE,
    creation_date        DATE NOT NULL,
    parent_department_id INTEGER REFERENCES department (id)
);
--rollback DROP TABLE department;