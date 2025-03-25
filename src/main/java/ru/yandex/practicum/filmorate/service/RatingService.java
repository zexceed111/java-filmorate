package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.RatingDbStorage;
import ru.yandex.practicum.filmorate.dto.RatingDto;
import ru.yandex.practicum.filmorate.dto.RatingRequest;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.RatingMapper;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class RatingService {

    @Autowired
    private final RatingDbStorage ratingDbStorage;

    public RatingDto createRating(RatingRequest request) {
        if (ratingDbStorage.existsByName(request.getName())) {
            log.warn("\nNot created rating {}", request);
            throw new DuplicateDataException("Rating " + request.getName() + " already exists.", request);
        }
        Rating rating = RatingMapper.mapToRating(request);
        return RatingMapper.mapToRatingDto(ratingDbStorage.createRating(rating));
    }

    public RatingDto changeRatingData(long id, RatingRequest request) {
        ratingDbStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA id = " + id + " not found ", request));
        Rating rating;
        rating = ratingDbStorage.findByName(request.getName()).orElse(null);
        if ((rating != null) && (rating.getId() != id)) {
            throw new DuplicateDataException("Name " + request.getName() + " is already used.", request);
        }
        rating = RatingMapper.mapToRating(request);
        Rating newRating = new Rating(id, rating.getName(), rating.getDescription());
        return RatingMapper.mapToRatingDto(ratingDbStorage.modifyRating(newRating));
    }

    public RatingDto deleteRating(long id) {
        Rating rating = ratingDbStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Rating id = " + id + " not founs ", id));
        return RatingMapper.mapToRatingDto(ratingDbStorage.deleteRating(rating));
    }

    public List<RatingDto> getAllRating() {
        return ratingDbStorage.getAllRating().stream().map(RatingMapper::mapToRatingDto).toList();
    }

    public RatingDto getRating(long l) {
        return RatingMapper.mapToRatingDto(ratingDbStorage.findById(l)
                .orElseThrow(() -> new NotFoundException("Rating id = " + l + " not found ", l)));
    }

}
