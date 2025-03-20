-- Очистка таблицы перед вставкой новых данных
DELETE FROM rating;
ALTER TABLE rating ALTER COLUMN id RESTART WITH 1;

-- Вставка данных без явного указания id (автоинкремент)
INSERT INTO rating (name, description) VALUES
    ('G', 'У фильма нет возрастных ограничений'),
    ('PG', 'Детям рекомендуется смотреть фильм с родителями'),
    ('PG-13', 'Детям до 13 лет просмотр не желателен'),
    ('R', 'Лица до 17 лет просматривать фильмы можно только в присутствии взрослого'),
    ('NC-17', 'Лицам до 18 лет просмотр запрещён');

-- Очистка жанров
DELETE FROM genre;
ALTER TABLE genre ALTER COLUMN id RESTART WITH 1;

-- Вставка данных в таблицу genre
INSERT INTO genre (name, description) VALUES
    ('Комедия', 'Комедия'),
    ('Драма', 'Драма'),
    ('Мультфильм', 'Мультфильм'),
    ('Триллер', 'Триллер'),
    ('Документальный', 'Документальный'),
    ('Боевик', 'Боевик');