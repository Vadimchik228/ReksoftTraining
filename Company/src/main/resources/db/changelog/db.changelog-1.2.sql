-- --liquibase formatted sql

-- --changeset rntgroup:1
ALTER SEQUENCE department_id_seq RESTART WITH 7;

ALTER SEQUENCE position_id_seq RESTART WITH 12;

ALTER SEQUENCE employee_id_seq RESTART WITH 20;