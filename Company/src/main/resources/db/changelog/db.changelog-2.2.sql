-- --liquibase formatted sql

-- --changeset rntgroup:1
ALTER SEQUENCE users_id_seq RESTART WITH 3;
