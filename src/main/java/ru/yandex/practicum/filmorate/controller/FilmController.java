package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    // строго говоря, первый публичный сеанс братья Люмьер провели 22.03.1895,
    // но задание есть задание
    private static final LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
    private static final int maxDescriptionLength = 200;
    public static Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public static @ResponseBody Film create(@Valid @RequestBody Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("\nSuccessfully created {}", film);
        return film;
    }

    @PutMapping
    public @ResponseBody Film update(@RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.info("\nNot updated {}", newFilm);
            throw new NotFoundException("Id должен быть указан", newFilm);
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() != null || !newFilm.getName().isBlank()) {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getReleaseDate() != null) {
                try {
                    if (!newFilm.getReleaseDate().isBefore(cinemaBirthday)) {
                        oldFilm.setReleaseDate(newFilm.getReleaseDate());
                    }
                } catch (RuntimeException e) {
                    log.info("\nIllegal release date ignored {}", newFilm);
                }
            }
            if (newFilm.getDescription().length() <= maxDescriptionLength)
                oldFilm.setDescription(newFilm.getDescription());
            if (newFilm.getDuration() > 0) oldFilm.setDuration(newFilm.getDuration());
            log.info("\nSuccessfully updated {}", oldFilm);
            return oldFilm;
        }
        log.info("\nNot updated {}", newFilm);
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден", newFilm);
    }

    private static long getNextId() {
        long currentMaxId = films.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }

}
