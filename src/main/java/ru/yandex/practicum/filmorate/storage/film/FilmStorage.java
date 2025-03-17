package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film addNewFilm(FilmRequest request);

    Film changeFilm(Film film);

    Film deleteFilm(Film film);

    List<Film> getAll();

    Film addLike(Long filmId, Long userId);

    Film deleteLike(Long filmId, Long userId);

    Optional<Film> findById(long filmId);

    Optional<Film> findByName(String name);

    List<Film> getPopular(long count);

    Optional<Film> findFilmWithLike(Long filmId, Long userId);

}