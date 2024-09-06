--liquibase formatted sql

--changeset rntgroup:1
CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
--rollback DROP TABLE users;

--changeset rntgroup:2
CREATE TABLE IF NOT EXISTS users_roles
(
    user_id BIGINT       NOT NULL,
    role    VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_users_roles_users FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);
--rollback DROP TABLE users_roles;