package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;
import java.util.Optional;

@Component("ratingDbStorage")
public class RatingDbStorage extends BaseRepository<Rating> implements RatingStorage {
    JdbcTemplate jdbcTemplate;

    private static final String CREATE_RATING_QUERY = "INSERT INTO rating(name, description) VALUES (?, ?)";
    private static final String MODIFY_RATING_QUERY = "UPDATE rating SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_RATING_QUERY = "DELETE FROM rating WHERE id = ?";
    private static final String GET_ALL_RATING = "SELECT * FROM rating ORDER BY id";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM rating WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM rating WHERE name = ?";
    private static final String EXISTS_BY_NAME = "SELECT EXISTS(SELECT 1 FROM rating WHERE name = ?)";

    public RatingDbStorage(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Rating createRating(Rating rating) {
        insert(
                CREATE_RATING_QUERY,
                rating.getName(),
                rating.getDescription()
        );
        return findByName(rating.getName())
                .orElseThrow(() -> new InternalServerException("Ошибка при чтении данных rating"));
    }

    @Override
    public Rating modifyRating(Rating rating) {
        update(
                MODIFY_RATING_QUERY,
                rating.getName(),
                rating.getDescription(),
                rating.getId()
        );
        return rating;
    }

    @Override
    public Rating deleteRating(Rating rating) {
        if (delete(DELETE_RATING_QUERY, rating.getId()))
            return rating;
        else
            throw new InternalServerException("Не удалось удалить " + rating);
    }

    @Override
    public List<Rating> getAllRating() {
        return findMany(GET_ALL_RATING);
    }

    @Override
    public Optional<Rating> findById(Long ratingId) {
        return findOne(FIND_BY_ID_QUERY, ratingId);
    }

    @Override
    public Optional<Rating> findByName(String name) {
        return findOne(FIND_BY_NAME_QUERY, name);
    }

    @Override
    public boolean existsByName(String name) {
        return jdbcTemplate.queryForObject(EXISTS_BY_NAME, Boolean.class, name);
    }

}