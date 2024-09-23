--liquibase formatted sql

--changeset rntgroup:1
CREATE TABLE department_snapshot
(
    id   INTEGER PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);
--rollback DROP TABLE department_snapshot;