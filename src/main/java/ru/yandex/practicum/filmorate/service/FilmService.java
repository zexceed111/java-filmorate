package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    private static final LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
    private static final int maxDescriptionLength = 200;

    private final InMemoryFilmStorage inMemoryFilmStorage;

    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public List<Film> getAll() {
        return inMemoryFilmStorage.getAllFilms().stream().toList();
    }

    public List<Film> getPopularFilms(long count) {
        log.info("Fetching top {} popular films", count);
        return getAll().stream()
                .sorted(Comparator.comparingInt(f -> -f.getUsersLikes().size()))
                .limit(count)
                .toList();
    }

    public Film addNewFilm(Film newFilm) {
        newFilm.setId(inMemoryFilmStorage.getNextId());
        log.debug("Adding new film: {}", newFilm);
        return inMemoryFilmStorage.addNewFilm(newFilm.getId(), newFilm);
    }

    public Film modifyFilm(Film film) {
        if (film.getId() == null) {
            throw new NotFoundException("Id фильма должен быть указан: " + film, film);
        }

        Film preparedFilm = new Film();
        preparedFilm.setId(film.getId());

        if (film.getName() != null && !film.getName().isBlank()) {
            preparedFilm.setName(film.getName());
        }

        if (film.getReleaseDate() != null && !film.getReleaseDate().isBefore(cinemaBirthday)) {
            preparedFilm.setReleaseDate(film.getReleaseDate());
        }

        if (film.getDescription() != null && film.getDescription().length() <= maxDescriptionLength) {
            preparedFilm.setDescription(film.getDescription());
        }

        if (film.getDuration() != null && film.getDuration() > 0) {
            preparedFilm.setDuration(film.getDuration());
        }

        log.info("Фильм успешно обновлен: {}", preparedFilm);
        return inMemoryFilmStorage.changeFilm(preparedFilm);
    }

    public void deleteFilm(Long filmId) {
        if (inMemoryFilmStorage.films.containsKey(filmId)) {
            inMemoryFilmStorage.deleteFilm(filmId);
        } else {
            throw new NotFoundException("Не найден фильм с идентификатором = " + filmId, filmId);
        }
    }

    public List<User> addUsersLike(Long filmId, Long userId) {
        return inMemoryFilmStorage.addLike(filmId, userId);
    }

    public List<User> deleteUsersLike(Long filmId, Long userId) {
        return inMemoryFilmStorage.deleteLike(filmId, userId);
    }

}