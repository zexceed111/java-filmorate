package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.dto.RatingRequest;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RatingDto create(@Valid @RequestBody RatingRequest request) {
        log.info("\nCreation rating {}", request);
        return ratingService.createRating(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingDto update(@PathVariable("rating") @Positive(message = "Rating's Id must be positive") long id, @Valid @RequestBody RatingRequest request) {
        log.info("\nUpdating rating {}", id);
        return ratingService.changeRatingData(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingDto deleteRating(@PathVariable @Positive(message = "Rating's Id must be positive") long id) {
        return ratingService.deleteRating(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RatingDto> getAll() {
        return ratingService.getAllRating();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingDto getRating(@PathVariable @Positive(message = "Rating's Id must be positive") long id) {
        return ratingService.getRating(id);
    }
}