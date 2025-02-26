INSERT INTO community(id, name) VALUES
    (1, 'Rocinante'),
    (2, 'Ceres'),
    (3, 'Earth'),
    (4, 'Luna');

INSERT INTO neighbor(id, community_id, name, mod) VALUES
    (1, 1, 'Holden', TRUE),
    (2, 1, 'Naomi', FALSE),
    (3, 1, 'Alex', FALSE),
    (4, 2, 'Miller', TRUE),
    (5, 2, 'Alexis', TRUE),
    (6, 3, 'Crisjen', TRUE),
    (7, 4, 'Jules-Pierre', FALSE);

INSERT INTO wish(id, expiration_date, neighbor_id) VALUES
    (1, NULL, 3),
    (2, NULL, 4),
    (3, date('2022-01-24'), 1),
    (4, date('2022-01-25'), 2);

INSERT INTO item(id, name, wish_id, neighbor_id, post_date) VALUES
    (1, 'spaceship', 1, 3, '2022-01-4'),
    (2, 'whiskey', 2, 4, '2022-02-10'),
    (3, 'protomolecule', 3, 1, '2022-02-12'),
    (4, 'water', 4, 2, '2022-01-12');

INSERT INTO nearby(community_a, community_b) VALUES
    (3, 4);