package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingStorage {

    Rating createRating(Rating rating);

    Rating modifyRating(Rating rating);

    Rating deleteRating(Rating rating);

    List<Rating> getAllRating();

    Optional<Rating> findById(Long ratingId);

    Optional<Rating> findByName(String name);

}