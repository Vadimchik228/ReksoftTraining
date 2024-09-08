--liquibase formatted sql

--changeset rntgroup:1
CREATE TABLE IF NOT EXISTS department_payroll
(
    id            BIGSERIAL PRIMARY KEY,
    department_id INTEGER        NOT NULL REFERENCES department (id) ON DELETE CASCADE,
    salary_fund   DECIMAL(15, 2) NOT NULL,
    timestamp     TIMESTAMP      NOT NULL
);
--rollback DROP TABLE department_payroll;
