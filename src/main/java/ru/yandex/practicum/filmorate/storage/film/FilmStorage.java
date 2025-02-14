package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    List<Film> getPopularFilms(long count);

    Film addNewFilm(Long id, Film newFilm);

    void deleteFilm(Long filmId);

    Film changeFilm(Film film) throws NotFoundException;

    List<User> addLike(Long filmId, Long userId) throws NotFoundException;

    List<User> deleteLike(Long filmId, Long userId) throws NotFoundException;
}