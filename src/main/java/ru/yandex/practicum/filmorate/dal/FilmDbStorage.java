package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {

    private static final String INSERT_FILM_QUERY =
            "INSERT INTO film (name, description, duration, release_date, mpa) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM_QUERY =
            "UPDATE film SET name = ?, description = ?, duration = ?, release_date = ?, mpa = ? WHERE id = ?";
    private static final String DELETE_FILM_QUERY = "DELETE FROM film WHERE id = ?";
    private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM film";
    private static final String INSERT_NEW_LIKE_QUERY = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_FILM_WITH_LIKE_QUERY =
            "SELECT * FROM film WHERE id IN (SELECT film_id FROM likes WHERE film_id = ? AND user_id = ?)";

    private static final String GET_POPULAR_FILMS_QUERY =
            "SELECT f.id, name, description, duration, release_date, mpa, COUNT(l.film_id) as count " +
                    "FROM film f LEFT JOIN likes l ON f.id = l.film_id GROUP BY f.id ORDER BY count desc, f.name LIMIT ?";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM film WHERE name = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
        //this.filmWithLikesStorage = filmWithLikesStorage;
    }

    public Film addNewFilm(FilmRequest request) {
        log.warn("\nAdding = {}", request);
        Long id = insert(
                INSERT_FILM_QUERY,
                request.getName(),
                request.getDescription(),
                request.getDuration(),
                request.getReleaseDate(),
                request.getMpa().getId()
        );
        Film film = findById(id)
                .orElseThrow(() -> new InternalServerException("Ошибка при чтении данных фильма"));
        return film;
    }

    public Film changeFilm(Film film) {
        log.warn("\nUpdating {}", film);
        update(
                UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpaId(),
                film.getId()
        );
        return film;
    }

    public Film deleteFilm(Film film) {
        if (delete(DELETE_FILM_QUERY, film.getId()))
            return film;
        else
            throw new InternalServerException("Не удалось удалить " + film);
    }

    public List<Film> getAll() {
        return findMany(GET_ALL_FILMS_QUERY);
    }

    public List<Film> getPopular(long count) {
        return findMany(GET_POPULAR_FILMS_QUERY, count);
    }

    @Override
    public Optional<Film> findFilmWithLike(Long filmId, Long userId) {
        return findOne(GET_FILM_WITH_LIKE_QUERY, filmId, userId);
    }

    public Film addLike(Long filmId, Long userId) {
        insert(INSERT_NEW_LIKE_QUERY, filmId, userId);
        return findById(filmId).orElseThrow(() -> new InternalServerException("Ошибка при чтении данных фильма"));
    }

    public Film deleteLike(Long filmId, Long userId) {
        Film film = new Film();
        if (delete(DELETE_LIKE_QUERY, filmId, userId))
            film = findById(filmId).orElseThrow(() -> new InternalServerException("Ошибка при чтении данных фильма"));
        else
            throw new InternalServerException("Не удалось удалить " + film);
        return film;
    }

    public Optional<Film> findById(long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public Optional<Film> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }

}
