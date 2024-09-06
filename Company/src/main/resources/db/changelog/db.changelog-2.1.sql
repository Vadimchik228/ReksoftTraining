--liquibase formatted sql

--changeset rntgroup:1
INSERT INTO users (name, username, password)
VALUES ('Vadim Schebetovskiy', 'schebetovskiyvadim@gmail.com', -- 123vadim!
        '$2a$12$JjEnD1/a.ANTqj19ON0aT.oYbgjFcrRkSPc3GKLCYXEpXlzv2dD8i'),
       ('Mike Wazowski', 'mikewazowski@yahoo.com', -- 123mike!
        '$2a$12$jjw6QNgcqr/uEAOE.qN6G.TYbs6HSmNQqhCx0PVUtefYMdB4DERQ2');

INSERT INTO users_roles (user_id, role)
VALUES (1, 'ADMIN'),
       (1, 'USER'),
       (2, 'USER');