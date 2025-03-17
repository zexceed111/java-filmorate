package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Slf4j
@Component("inMemoryFilmStorage")
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final InMemoryUserStorage inMemoryUserStorage;
    public Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getAll() {
        return films.values().stream().toList();
    }

    @Override
    public List<Film> getPopular(long count) {
        return films.keySet().stream()
                .map(i -> films.get(i))
                .sorted(new Comparator<Film>() {
                    public int compare(Film f1, Film f2) {
                        return (-(f1.getUsersLikes().size() - f2.getUsersLikes().size()));
                    }
                })
                .limit(count)
                .toList();
    }

    public List<User> getFilmsLikes(Long filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Film with id=" + filmId + " not found", filmId);
        }
        return films.get(filmId).getUsersLikes().stream()
                .map(inMemoryUserStorage.getUsers()::get)
                .toList();
    }

    @Override
    public Film addNewFilm(FilmRequest request) {
        long id = getNextId();
        Film film = FilmMapper.mapToFilm(request);
        film.setId(id);
        films.put(id, film);
        log.info("\nSuccessfully created {}", film);
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        films.remove(film.getId());
        log.info("\nSuccessfully deleted {}", film);
        return film;
    }

    @Override
    public Film changeFilm(Film film) {
        Film oldFilm = films.get(film.getId());
        if (film.getName() != null)
            oldFilm.setName(film.getName());
        if (film.getReleaseDate() != null)
            oldFilm.setReleaseDate(film.getReleaseDate());
        if (film.getDescription() != null)
            oldFilm.setDescription(film.getDescription());
        if (film.getDuration() != null)
            oldFilm.setDuration(film.getDuration());
        films.put(oldFilm.getId(), oldFilm);
        log.info("\nSuccessfully changed {}", oldFilm);
        return oldFilm;
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = new Film();
        film = films.get(filmId);
        Set<Long> tmpLikes = film.getUsersLikes();
        tmpLikes.add(userId);
        film.setUsersLikes(tmpLikes);
        films.put(filmId, film);
        return films.get(filmId);
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
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
        return films.get(filmId);
    }

    public long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    // Имплементация методов, добавленных в FilmStorage
    @Override
    public Optional<Film> findById(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Optional<Film> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Film> findFilmWithLike(Long filmId, Long userId) {
        return
                films.get(filmId).getUsersLikes().contains(userId)
                        ? Optional.ofNullable(films.get(filmId))
                        : Optional.empty();
    }

}