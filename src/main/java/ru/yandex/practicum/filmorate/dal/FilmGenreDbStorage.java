package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenre.FilmGenreStorage;

import java.util.List;

@Component("filmGenreDbStorage")
public class FilmGenreDbStorage extends BaseRepository<FilmGenre> implements FilmGenreStorage {

    private static final String GET_FILMS_GENRES_QUERY = "SELECT * FROM film_genre WHERE film_id = ?";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_FILMS_GENRES_QUERY = "DELETE FROM film_genre WHERE film_id = ?";

    public FilmGenreDbStorage(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmGenre> getGenresOfFilm(Long id) {
        return findMany(GET_FILMS_GENRES_QUERY, id);
    }

    @Override
    public void addFilmGenres(Long id, List<Long> genres) {
        if (genres == null || genres.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

        List<Object[]> batchArgs = genres.stream().map(genreId -> new Object[]{id, genreId}).toList();

        jdbc.batchUpdate(sql, batchArgs);
    }


    @Override
    public void deleteGenreOfFilms(long id) {
        delete(DELETE_FILMS_GENRES_QUERY, id);
    }
}