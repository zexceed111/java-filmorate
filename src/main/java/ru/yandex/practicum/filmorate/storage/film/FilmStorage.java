package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    Film addNewFilm(Long id, Film newFilm);

    void deleteFilm(Long filmId);

    Film changeFilm(Film film);

    List<User> addLike(Long filmId, Long userId);

    List<User> deleteLike(Long filmId, Long userId);
}