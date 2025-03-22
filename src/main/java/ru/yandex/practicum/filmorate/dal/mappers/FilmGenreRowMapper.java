package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("filmGenreRowMapper")
public class FilmGenreRowMapper implements RowMapper<FilmGenre> {
    @Override
    public FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmGenre fg = new FilmGenre();
        fg.setId(rs.getLong("id"));
        fg.setFilmId(rs.getLong("film_id"));
        fg.setGenreId(rs.getLong("genre_id"));
        return fg;
    }
}
