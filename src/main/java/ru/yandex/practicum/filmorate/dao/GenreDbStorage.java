package ru.yandex.practicum.filmorate.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@RequiredArgsConstructor
@Repository
@FieldDefaults(level = AccessLevel.PRIVATE,
        makeFinal = true)
public class GenreDbStorage {
    JdbcTemplate jdbcTemplate;


    public List<Genre> findAllGenres() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }


    public Optional<Genre> findGenreById(int id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id).stream().findFirst();
    }


    public void findAllGenresByFilm(List<Film> films) {
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        String sql = "SELECT * FROM GENRES g, film_genres fg WHERE fg.genre_id = g.genre_id AND fg.film_id IN (%s)";
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        jdbcTemplate.query(String.format(sql, inSql),
                filmById.keySet().toArray(),
                (rs, rowNum) -> filmById.get(rs.getInt("film_id")).getGenres().add(makeGenre(rs)));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}