package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.GenreRequest;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDto create(@Valid @RequestBody GenreRequest request) {
        log.info("\nCreation genre {}", request);
        return genreService.createGenre(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto update(@PathVariable("genreId") @Positive(message = "Genre Id must be positive") long id,
                           @Valid @RequestBody GenreRequest request) {
        log.info("\nUpdating genre {}", id);
        return genreService.changeGenreData(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto deleteGenre(@PathVariable @Positive(message = "Genre Id must be positive") long id) {
        return genreService.deleteGenre(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GenreDto> getAll() {
        log.info("\nGetting all genres");
        return genreService.getAllGenre();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreDto getOneGenre(@PathVariable @Positive(message = "Genre Id must be positive") long id) {
        return genreService.getGenreById(id);
    }

}