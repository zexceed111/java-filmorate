INSERT INTO genre (name, description)
          VALUES
              ('Horror', 'Ужасы'),
              ('Fantastic', 'Фантастика'),
              ('Детектив', 'Расследование преступлений');

INSERT INTO film (name, description, release_date, duration, mpa)
          VALUES
              ('A1', 'Empty', '1990-01-01', 75, 1),
              ('B2', 'Em22', '1991-02-02', 80, 2),
              ('C3', 'Full1', '1992-03-03', 111, 3),
              ('D4', 'flkffdg', '1993-04-04', 92, 2),
              ('F6', 'FullFull', '1995-06-06', 77, 3),
              ('G7', 'erwdfj', '1996-07-07', 92, 4),
              ('H8', 'lkfsffs', '1997-08-08', 79, 4),
              ('I9', 'wefjldpfo', '1998-09-09', 102, 2),
              ('Экипаж', 'СССР', '1980-06-15', 140, 1),
              ('Место встречи изменить нельзя', 'детектив', '1979-03-01', 390, 1),
              ('E5', 'dfklj', '1994-05-05', 89, 5);

INSERT INTO users (email, login, name, birthday)
          VALUES
              ('b@r2.com', 'beta', 'Bet', '1999-09-09'),
              ('a@rr.com', 'log1', 'Alf', '1998-08-08'),
              ('c@vr.com', 'gamma', 'Gram', '2000-10-10'),
              ('d@fdd.net', 'delta', 'Del', '2010-09-10'),
              ('e@fhdj.com', 'epsilon', 'Eps', '2001-01-01'),
              ('f@ddd.net', 'fi', 'Fi', '2002-02-02'),
              ('g@ggg.net', 'ksi', 'Ksi', '2003-03-03'),
              ('h@hhh.com', 'lambda', 'Lambda1', '2004-04-04');

INSERT INTO likes (film_id, user_id)
          VALUES
              (1, 2),
              (2, 1),
              (2, 2),
              (3, 1),
              (3, 3),
              (3, 4),
              (4, 2),
              (4, 4),
              (7, 1),
              (7, 3),
              (7, 4),
              (7, 6),
              (3, 6);

INSERT INTO friends (user1_id, user2_id)
          VALUES
              (2, 3),
              (4, 3),
              (5, 2),
              (5, 4),
              (6, 2),
              (1, 3),
              (1, 2),
              (4, 5);

INSERT INTO film_genre (film_id, genre_id)
          VALUES
              (1, 2),
              (2, 1),
              (2, 2),
              (3, 1),
              (3, 3),
              (3, 4),
              (4, 2),
              (4, 4),
              (7, 1),
              (7, 3),
              (7, 4),
              (7, 6),
              (3, 6);