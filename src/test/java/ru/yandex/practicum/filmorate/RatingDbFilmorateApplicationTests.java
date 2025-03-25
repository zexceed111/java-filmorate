package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dal.RatingDbStorage;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDbFilmorateApplicationTests {
    private final RatingDbStorage ratingStorage;

    @Test
    public void testFindById() {

        Optional<Rating> ratingOptional = ratingStorage.findById(1L);

        assertThat(ratingOptional)
                .isPresent()
                .hasValueSatisfying(rating ->
                        assertThat(rating).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindByName() {

        Optional<Rating> ratingOptional = ratingStorage.findByName("PG-13");

        assertThat(ratingOptional)
                .isPresent()
                .hasValueSatisfying(rating -> {
                            assertThat(rating).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(rating).hasFieldOrPropertyWithValue("name", "PG-13");
                        }
                );
    }

    @Test
    public void testCreateRating() {

        Rating rating = new Rating();
        rating.setName("Abraka");
        rating.setDescription("Nothing");

        Rating createdRating = ratingStorage.createRating(rating);
        assertThat(createdRating).hasFieldOrPropertyWithValue("name", rating.getName());
        assertThat(createdRating).hasFieldOrPropertyWithValue("description", rating.getDescription());
    }

    @Test
    public void testModifyRating() {

        Rating rating = new Rating(1L, "G1", "All");
        ratingStorage.modifyRating(rating);

        Rating modifiedRating = ratingStorage.findById(1L).orElse(null);
        assertThat(modifiedRating == null).isEqualTo(false);
        assertThat(modifiedRating).hasFieldOrPropertyWithValue("name", rating.getName());
        assertThat(modifiedRating).hasFieldOrPropertyWithValue("description", rating.getDescription());
    }

    @Test
    public void testDeleteRating() {
        Rating rating = new Rating();
        rating.setName("Abraka");
        rating.setDescription("Nothing");
        ratingStorage.createRating(rating);
        Rating mpa = ratingStorage.findByName(rating.getName())
                .orElseThrow(() -> new InternalServerException("rating not found"));
        Long id = mpa.getId();
        ratingStorage.deleteRating(mpa);
        Rating testRating = ratingStorage.findById(id).orElse(null);
        assertThat(testRating == null).isEqualTo(true);
    }

    @Test
    public void testGetAllRating() {
        Rating rating = ratingStorage.findById(3L).orElseThrow(() -> new InternalServerException("user not found"));
        Rating rating2 = ratingStorage.findById(4L).orElseThrow(() -> new InternalServerException("user not found"));

        List<Rating> ratings = ratingStorage.getAllRating();
        assertThat(ratings.isEmpty()).isEqualTo(false);
        assertThat(ratings).contains(rating);
        assertThat(ratings).contains(rating2);
    }
}
