--liquibase formatted sql

--changeset rntgroup:1
CREATE TABLE IF NOT EXISTS position
(
    id   SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);
--rollback DROP TABLE position;

--changeset rntgroup:2
CREATE TABLE IF NOT EXISTS employee
(
    id              BIGSERIAL PRIMARY KEY,
    last_name       VARCHAR(35)                        NOT NULL,
    first_name      VARCHAR(35)                        NOT NULL,
    patronymic      VARCHAR(35),
    sex             VARCHAR(6)                         NOT NULL,
    birth_date      DATE                               NOT NULL,
    phone_number    VARCHAR(20)                        NOT NULL,
    employment_date DATE                               NOT NULL,
    dismissal_date  DATE,
    position_id     INTEGER REFERENCES position (id)   NOT NULL,
    salary          DECIMAL(10, 2)                     NOT NULL,
    director        BOOLEAN                            NOT NULL,
    department_id   INTEGER                            NOT NULL
);
--rollback DROP TABLE employee;