CREATE TABLE users
(
    id       INTEGER PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    surname  VARCHAR(100) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    seed     VARCHAR(255)
);

