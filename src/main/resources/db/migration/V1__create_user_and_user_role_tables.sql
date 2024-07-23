CREATE TABLE user_role
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    name        VARCHAR(20) UNIQUE,
    description VARCHAR(300)
);

CREATE TABLE users
(
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(200) NOT NULL,
    role_id    BIGINT       NOT NULL,
    CONSTRAINT fk_user_user_role FOREIGN KEY (role_id) REFERENCES user_role (id)
)