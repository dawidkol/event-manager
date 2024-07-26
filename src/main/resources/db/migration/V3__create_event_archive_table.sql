CREATE TABLE event_archive
(
    id       BIGINT NOT NULL PRIMARY KEY,
    event_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_event_archive_event FOREIGN KEY (event_id) REFERENCES event (id)
)