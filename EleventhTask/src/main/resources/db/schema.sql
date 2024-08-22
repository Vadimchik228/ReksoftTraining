CREATE TABLE users
(
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    surname   VARCHAR(255) NOT NULL,
    birthdate DATE         NOT NULL
);

CREATE TABLE friendships
(
    userId1   BIGINT NOT NULL,
    userId2   BIGINT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (userId1, userId2),
    FOREIGN KEY (userId1) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (userId2) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE posts
(
    id        BIGSERIAL PRIMARY KEY,
    userId    BIGINT NOT NULL,
    text      TEXT   NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE likes
(
    postId    BIGINT    NOT NULL,
    userId    BIGINT    NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (postId, userId),
    FOREIGN KEY (postId) REFERENCES posts (id) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES users (id) ON DELETE CASCADE
);
