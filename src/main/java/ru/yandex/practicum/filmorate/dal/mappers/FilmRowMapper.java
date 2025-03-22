package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getLong("duration"));

        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setMpaId(resultSet.getLong("mpa"));
        try {
            film.setCount(resultSet.getInt("count"));
        } catch (Exception ignored) {
        }
        return film;
    }
}
