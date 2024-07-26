CREATE TABLE event
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY ,
    name        VARCHAR(30)  NOT NULL,
    description VARCHAR(200) NOT NULL,
    event_start TIMESTAMP    NOT NULL,
    event_end   TIMESTAMP    NOT NULL,
    price       NUMERIC(38, 2) DEFAULT 0
);

CREATE TABLE event_user
(
    event_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    CONSTRAINT fk_event_user_event FOREIGN KEY (event_id) REFERENCES event (id),
    CONSTRAINT fk_event_user_user FOREIGN KEY (user_id) REFERENCES users (id)
)