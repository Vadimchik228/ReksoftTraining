--liquibase formatted sql

--changeset rntgroup:1
CREATE TABLE IF NOT EXISTS ruler
(
    id         SERIAL PRIMARY KEY,
    last_name  VARCHAR(35) NOT NULL,
    first_name VARCHAR(35) NOT NULL,
    patronymic VARCHAR(35),
    nickname   VARCHAR(35),
    birth_date DATE,
    death_date DATE,
    century    INTEGER     NOT NULL
);
--rollback DROP TABLE ruler;

--changeset rntgroup:2
CREATE TABLE IF NOT EXISTS country
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(35) NOT NULL UNIQUE
);
--rollback DROP TABLE country;

--changeset rntgroup:3
CREATE TABLE IF NOT EXISTS territory_type
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(35) NOT NULL UNIQUE
);
--rollback DROP TABLE territory_type;

--changeset rntgroup:4
CREATE TABLE IF NOT EXISTS territorial_unit
(
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(35) NOT NULL,
    territory_type_id INTEGER     NOT NULL
        REFERENCES
            territory_type (id),
    foundation_date   DATE        NOT NULL,
    country_id        INTEGER
        REFERENCES
            country (id),
    population        INTEGER     NOT NULL
);
--rollback DROP TABLE territorial_unit;

--changeset rntgroup:5
CREATE TABLE IF NOT EXISTS site
(
    id                  SERIAL PRIMARY KEY,
    address             TEXT    NOT NULL,
    territorial_unit_id INTEGER NOT NULL
        REFERENCES
            territorial_unit (id)
);
--rollback DROP TABLE site;

--changeset rntgroup:6
CREATE TABLE IF NOT EXISTS ruler_territorial_unit
(
    id                  SERIAL PRIMARY KEY,
    ruler_id            INTEGER NOT NULL
        REFERENCES
            ruler (id),
    territorial_unit_id INTEGER NOT NULL
        REFERENCES
            territorial_unit (id),
    start_date          DATE,
    end_date            DATE,
    century             INTEGER NOT NULL
);
--rollback DROP TABLE ruler_territorial_unit;
