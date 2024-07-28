INSERT INTO event (name, description, event_start, event_end, price)
VALUES ('Networking J', 'Networking event with industry leaders and professionals.', now() - INTERVAL '1 day',
        now() + INTERVAL '1day', 75.00);