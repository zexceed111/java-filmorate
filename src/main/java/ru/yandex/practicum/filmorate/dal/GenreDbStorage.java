package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Component("genreDbStorage")
public class GenreDbStorage extends BaseRepository<Genre> {

    private static final String CREATE_GENRE_QUERY = "INSERT INTO genre(name, description) VALUES (?, ?)";
    private static final String MODIFY_GENRE_QUERY = "UPDATE genre SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_GENRE_QUERY = "DELETE FROM genre WHERE id = ?";
    private static final String GET_ALL_GENRE = "SELECT * FROM genre ORDER BY id";
    private static final String GET_FILM_GENRES_QUERY = "SELECT g.id, g.name, g.description FROM film_genre " +
            "LEFT JOIN genre g ON genre_id = g.id WHERE film_id = ? ORDER BY g.id";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genre WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM genre WHERE name = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Genre createGenre(Genre genre) {
        insert(
                CREATE_GENRE_QUERY,
                genre.getName(),
                genre.getDescription()
        );
        return findByName(genre.getName())
                .orElseThrow(() -> new InternalServerException("Ошибка при чтении данных genre"));
    }

    public Genre modifyGenre(Genre genre) {
        update(
                MODIFY_GENRE_QUERY,
                genre.getName(),
                genre.getDescription(),
                genre.getId()
        );
        return genre;
    }

    public List<Genre> findByFilmId(Long l) {
        return findMany(GET_FILM_GENRES_QUERY, l);
    }

    public Genre deleteGenre(Genre genre) {
        if (delete(DELETE_GENRE_QUERY, genre.getId()))
            return genre;
        else
            throw new InternalServerException("Не удалось удалить " + genre);
    }

    public List<Genre> getAllGenre() {
        return findMany(GET_ALL_GENRE);
    }

    public Optional<Genre> findById(long genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    public Optional<Genre> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }


}