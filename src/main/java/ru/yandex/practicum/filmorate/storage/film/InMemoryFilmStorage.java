package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final InMemoryUserStorage inMemoryUserStorage;
    public Map<Long, Film> films = new HashMap<>();

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    //В задании этого метода не было, но он потребовался по ходу выполнения
    public List<User> getFilmsLikes(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Film with id=" + filmId + " not found", filmId);
        }
        return films.get(filmId).getUsersLikes().stream().map(inMemoryUserStorage.getUsers()::get).toList();
    }

    @Override
    public Film addNewFilm(Long id, Film newFilm) {
        films.put(id, newFilm);
        log.info("\nSuccessfully created {}", newFilm);
        return newFilm;
    }

    //Метод с удалением фильма реализовал случайно, в заданиях такого не было, но всё равно пригодится
    @Override
    public void deleteFilm(Long filmId) {
        Film film = films.get(filmId);
        films.remove(filmId);
        log.info("\nSuccessfully deleted {}", film);
    }

    @Override
    public Film changeFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("\nNot updated {}", film);
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден", film);
        }
        Film oldFilm = films.get(film.getId());
        if (film.getName() != null) oldFilm.setName(film.getName());
        if (film.getReleaseDate() != null) oldFilm.setReleaseDate(film.getReleaseDate());
        if (film.getDescription() != null) oldFilm.setDescription(film.getDescription());
        if (film.getDuration() != null) oldFilm.setDuration(film.getDuration());
        films.put(oldFilm.getId(), oldFilm);
        log.info("\nSuccessfully changed {}", oldFilm);
        return oldFilm;
    }

    @Override
    public List<User> addLike(Long filmId, Long userId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Film with id=" + filmId + " not found", filmId);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found", userId);
        }
        Film film = new Film();
        film = films.get(filmId);
        Set<Long> tmpLikes = film.getUsersLikes();
        tmpLikes.add(userId);
        film.setUsersLikes(tmpLikes);
        films.put(filmId, film);
        return getFilmsLikes(filmId);
    }

    @Override
    public List<User> deleteLike(Long filmId, Long userId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Film with id=" + filmId + " not found", filmId);
        }
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("User with id=" + userId + " not found", userId);
        }
        Film film = new Film();
        film = films.get(filmId);
        Set<Long> tmpLikes = film.getUsersLikes();
        tmpLikes.remove(userId);
        film.setUsersLikes(tmpLikes);
        films.put(filmId, film);
        return getFilmsLikes(filmId);
    }

    public long getNextId() {
        long currentMaxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }

}