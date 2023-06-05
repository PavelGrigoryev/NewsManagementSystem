--liquibase formatted sql

--changeset Grigoryev_Pavel:1
INSERT INTO users(id, firstname, lastname, email, password, created_time, updated_time, role)
VALUES (1, 'Чак', 'Норрис', 'ChakcNunChuck@gmail.com', '$2a$10$VtfFiijYXCgjuxCYV8B/GehFxaCtyaA6FF1HM/vNwzmNSmE3e6Rzq',
        '2023-06-06 16:45:59', '2023-06-06 16:45:59',
        'SUBSCRIBER'),
       (2, 'Арнольд', 'Шварценеггер', 'Shwarsz@yahoo.com',
        '$2a$10$bAUNy5W/U3Ckf7jlOU.wAuMJmcMwXSi7mpa0h/3M3JY2WXdThaabS',
        '2023-06-06 12:33:47', '2023-06-06 12:33:47',
        'JOURNALIST'),
       (3, 'Брюс', 'Ли', 'BruceLee@shazam.com', '$2a$10$31aS99q2Q91LMyY/CHLo2.os3zq9dadKGPkE9Kfz8ZKfnI4HAb6Xi',
        '2023-06-06 10:11:13', '2023-06-06 10:11:13',
        'ADMIN');

SELECT setval('users_id_seq', (SELECT max(id) FROM users));
