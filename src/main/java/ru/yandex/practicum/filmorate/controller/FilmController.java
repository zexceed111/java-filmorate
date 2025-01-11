package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();

    private long getNextId() {
        long currentMaxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        try {
            if (film.getName() == null || film.getName().isBlank()) {
                log.error("Валидация не пройдена: название фильма не может быть пустым.");
                throw new ValidationException("название фильма не может быть пустым.");
            }
            if (film.getDescription() != null && film.getDescription().length() > 200) {
                log.error("Валидация не пройдена: описание фильма не может превышать 200 символов.");
                throw new ValidationException("максимальная длина описания — 200 символов");
            }
            if (film.getDuration() <= 0) {
                log.error("Валидация не пройдена: продолжительность фильма должна быть положительным числом.");
                throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
            }

            LocalDate releaseDate = (LocalDate) film.getReleaseDate();
            LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
            if (releaseDate == null || releaseDate.isBefore(minReleaseDate)) {
                log.error("Валидация не пройдена: дата релиза не может быть раньше 28 декабря 1895 года.");
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
            }
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Фильм добавлен: {}", film);
            return new ResponseEntity<>(film, HttpStatus.CREATED);
        } catch (ValidationException e) {
            log.error("Ошибка при добавлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable Long id, @RequestBody Film film) {
        if (!films.containsKey(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        film.setId(id);
        films.put(id, film);
        log.info("Фильм обновлен {}", films);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        return new ResponseEntity<>(films.values(), HttpStatus.OK);
    }
}
