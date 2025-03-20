package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    @Autowired
    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@Valid @RequestBody FilmRequest request) {
        log.debug("\nCreation of {}", request);
        FilmDto fd = filmService.addNewFilm(request);
        log.info("Created {}", fd);
        return fd;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmDto update(@Valid @RequestBody FilmRequest request) {
        log.info("\nUpdating of {}", request);
        FilmDto fd = filmService.modifyFilm(request);
        log.info("Created {}", fd);
        return fd;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto deleteFilm(@Valid @PathVariable("id") @Positive(message = "Films Id must be positive") long id) {
        log.info("\nDeleting of film id = {}", id);
        return filmService.deleteFilm(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> findAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto findOneFilm(@Valid @PathVariable("id") @Positive(message = "Films Id must be positive") long id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<FilmDto> findFilms(@Valid @RequestParam(required = false, defaultValue = "10") @Positive Long count) {
        log.info("\nGetting {} most popular films", count);
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto addUsersLike(@Valid @PathVariable @Positive(message = "Films id must be positive") long filmId,
                                @Valid @PathVariable @Positive(message = "Users id must be positive") long userId) {
        log.info("\nAdding of like to film {} from user {}", filmId, userId);
        return filmService.addUsersLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmDto deleteUsersLike(@Valid @PathVariable @Positive(message = "Films id must be positive") long id,
                                   @Valid @PathVariable @Positive(message = "Users id must be positive") long userId) {
        log.info("\nDeleting of like to film {} from user {}", id, userId);
        return filmService.deleteUsersLike(id, userId);
    }

}