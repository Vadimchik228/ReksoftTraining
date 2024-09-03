INSERT INTO users (name, username, password)
VALUES ('Vadim Schebetovskiy', 'schebetovskiyvadim@gmail.com', -- 123
        '$2a$12$gqC8Xut8t3CFAFsr7lG8DuRCPsxmkWy8LnyNuhmcQ2eKURKdI.q4e'),
       ('Mike Wazowski', 'mikewazowski@yahoo.com', -- 321
        '$2a$12$Q6HmUPJa0r2VwPe24u8wCOVS9Srp37IrNnZa2eIlxs5h.IvibKqTe');

-- INSERT INTO users_roles (user_id, role)
-- VALUES (1, 'ADMIN'),
--        (1, 'USER'),
--        (2, 'USER');

SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));