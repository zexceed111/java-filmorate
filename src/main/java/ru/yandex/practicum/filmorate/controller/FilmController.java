package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/popular")
    public Collection<Film> findFilms(@RequestParam(required = false, defaultValue = "10") @Positive long count) {
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public List<User> addUsersLike(@PathVariable @Positive long id, @PathVariable @Positive long userId) {
        return filmService.addUsersLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public List<User> deleteUsersLike(@PathVariable @Positive long id, @PathVariable @Positive long userId) {
        log.info("\nDeleting of like to film {} from user {}", id, userId);
        return filmService.deleteUsersLike(id, userId);
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.getAll();
    }

    @PostMapping
    public @ResponseBody Film create(@Valid @RequestBody Film film) {
        log.info("\nCreation of {}", film);
        film = filmService.addNewFilm(film);
        log.info("\nSuccessfully created {}", film);
        return film;
    }

    @PutMapping
    public @ResponseBody Film update(@RequestBody Film newFilm) {
        log.info("Updating film {}", newFilm);
        return filmService.modifyFilm(newFilm);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam @Positive long id) {
        log.info("\nDeleting of film id={}", id);
        filmService.deleteFilm(id);
        log.info("\nSuccessfully deleted {}", id);
        return new ResponseEntity<>("Successfully deleted film: id=" + id, HttpStatus.OK);
    }

}