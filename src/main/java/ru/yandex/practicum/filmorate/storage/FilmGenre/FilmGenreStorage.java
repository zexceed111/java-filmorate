package ru.yandex.practicum.filmorate.storage.FilmGenre;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {

    List<FilmGenre> getGenresOfFilm(Long id);

    void addFilmGenres(Long id, List<Long> values);

    void deleteGenreOfFilms(long id);
}