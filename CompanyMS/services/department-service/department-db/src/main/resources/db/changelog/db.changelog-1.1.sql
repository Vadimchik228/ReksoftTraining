--liquibase formatted sql

--changeset rntgroup:1
CREATE TABLE IF NOT EXISTS revision
(
    id        SERIAL PRIMARY KEY,
    timestamp BIGINT NOT NULL
);
--rollback DROP TABLE revision;

--changeset rntgroup:2
CREATE TABLE IF NOT EXISTS department_aud
(
    id                   INTEGER NOT NULL,
    rev                  INTEGER NOT NULL,
    revtype              SMALLINT,
    creation_date        DATE,
    name                 TEXT,
    parent_department_id INTEGER,
    PRIMARY KEY (rev, id)
);
--rollback DROP TABLE department_aud;

ALTER TABLE department_aud
    ADD CONSTRAINT fk_revision_id FOREIGN KEY (rev) REFERENCES revision (id);
