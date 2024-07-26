-- Insert 10 records into the 'event' table with past dates

INSERT INTO event (name, description, event_start, event_end, price)
VALUES ('Conference A', 'Annual tech conference with various speakers and sessions.', '2023-08-01T09:00:00',
        '2023-08-01T17:00:00', 199.99),
       ('Workshop B', 'Hands-on workshop about new technology trends.', '2023-09-10T10:00:00', '2023-09-10T16:00:00',
        99.50),
       ('Webinar C', 'Online webinar on software development practices.', '2023-10-05T14:00:00', '2023-10-05T15:30:00',
        29.99),
       ('Seminar D', 'In-depth seminar on data science and analytics.', '2023-11-15T08:00:00', '2023-11-15T12:00:00',
        149.00),
       ('Meetup E', 'Casual meetup for tech enthusiasts and professionals.', '2023-12-01T18:00:00',
        '2023-12-01T21:00:00', 0.00),
       ('Course F', 'Complete course on advanced Java programming.', '2023-07-20T10:00:00', '2023-07-20T15:00:00',
        299.00),
       ('Lecture G', 'Guest lecture on machine learning innovations.', '2023-08-22T11:00:00', '2023-08-22T13:00:00',
        49.99),
       ('Hackathon H', '24-hour hackathon event for developers.', '2023-09-30T08:00:00', '2023-10-01T08:00:00', 150.00),
       ('Expo I', 'Technology expo showcasing the latest gadgets.', '2023-11-05T09:00:00', '2023-11-05T17:00:00',
        89.90),
       ('Networking J', 'Networking event with industry leaders and professionals.', '2023-12-15T19:00:00',
        '2023-12-15T22:00:00', 75.00);
