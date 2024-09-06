--liquibase formatted sql

--changeset rntgroup:1
ALTER TABLE employee
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE employee
    ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE employee
    ADD COLUMN created_by VARCHAR(255);

ALTER TABLE employee
    ADD COLUMN last_modified_by VARCHAR(255);