package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.dto.RatingRequest;
import ru.yandex.practicum.filmorate.model.Rating;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RatingMapper {
    public static Rating mapToRating(RatingRequest request) {
        Rating rating = new Rating();
        rating.setName(request.getName());
        rating.setDescription(request.getDescription());
        return rating;
    }

    public static RatingDto mapToRatingDto(Rating rating) {
        RatingDto dto = new RatingDto();
        dto.setId(rating.getId());
        dto.setName(rating.getName());
        dto.setDescription(rating.getDescription());
        return dto;
    }
}
