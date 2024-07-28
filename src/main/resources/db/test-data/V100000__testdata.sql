INSERT INTO user_role(name, description)
VALUES ('ADMIN', 'He''s like a GOD'),
       ('USER', 'Simple user');

INSERT INTO users(first_name, last_name, email, password, phone_number, role_id)
VALUES ('John', 'Doe', 'john.doe@test.pl', '{noop}password', '+48669964099', 2),
       ('Janusz', 'Kowalski', 'janusz.kowalski@test.pl', '{noop}password', '+48669964099', 2),
       ('Anna', 'Zawadzka', 'anna.zawadzka@test.pl', '{noop}password', '+48669964099', 2);

INSERT INTO event (name, description, event_start, event_end, price)
VALUES ('Conference A', 'Annual tech conference with various speakers and sessions.', now() - INTERVAL '1 day',
        now() + INTERVAL '1day', 199.99),
       ('Workshop B', 'Hands-on workshop about new technology trends.', now() + INTERVAL '1 day',
        now() + INTERVAL '2 day',
        99.50),
       ('Webinar C', 'Online webinar on software development practices.', now() + INTERVAL '1 day',
        now() + INTERVAL '2 day',
        29.99),
       ('Seminar D', 'In-depth seminar on data science and analytics.', now() + INTERVAL '1 day',
        now() + INTERVAL '4 day',
        149.00),
       ('Meetup E', 'Casual meetup for tech enthusiasts and professionals.', now() + INTERVAL '1 day',
        now() + INTERVAL '5 day', 0.00),
       ('Course F', 'Complete course on advanced Java programming.', now() + INTERVAL '4 day', now() + INTERVAL '5 day',
        299.00),
       ('Lecture G', 'Guest lecture on machine learning innovations.', now() + INTERVAL '6 day',
        now() + INTERVAL '7 day',
        49.99),
       ('Hackathon H', '24-hour hackathon event for developers.', now() + INTERVAL '10 day', now() + INTERVAL '11 day',
        150.00),
       ('Expo I', 'Technology expo showcasing the latest gadgets.', now() + INTERVAL '10 day',
        now() + INTERVAL '11 day',
        89.90),
       ('Networking J', 'Networking event with industry leaders and professionals.', now() - INTERVAL '10 day',
        now() + INTERVAL '2day', 75.00);

INSERT INTO event_user (event_id, user_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (3, 1),
       (4, 2),
       (5, 3);

