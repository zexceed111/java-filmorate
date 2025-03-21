package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbFilmoRateApplicationTests {

    private final GenreDbStorage genreDbStorage;

    @Test
    public void testFindGenreById() {

        Optional<Genre> genreOptional = genreDbStorage.findById(1);

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindGenreByName() {

        Optional<Genre> genreOptional = genreDbStorage.findByName("Мультфильм");

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> {
                            assertThat(genre).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(genre).hasFieldOrPropertyWithValue("name", "Мультфильм");
                        }
                );
    }

    @Test
    public void testCreateGenre() {

        Genre genre = new Genre();
        genre.setName("Abrakadabra");
        genre.setDescription("Nothing");

        Genre createdGenre = genreDbStorage.createGenre(genre);
        assertThat(createdGenre).hasFieldOrPropertyWithValue("name", genre.getName());
        assertThat(createdGenre).hasFieldOrPropertyWithValue("description", genre.getDescription());
    }

    @Test
    public void testModifyGenre() {

        Genre genre = new Genre(1L, "NonAbrakadabra", "All");
        genreDbStorage.modifyGenre(genre);

        Genre modifiedGenre = genreDbStorage.findById(1L).orElse(null);
        assertThat(modifiedGenre == null).isEqualTo(false);
        assertThat(modifiedGenre).hasFieldOrPropertyWithValue("name", genre.getName());
        assertThat(modifiedGenre).hasFieldOrPropertyWithValue("description", genre.getDescription());
    }

    @Test
    public void testDeleteGenre() {
        Genre genre = genreDbStorage.findById(8L)
                .orElseThrow(() -> new InternalServerException("genre not found"));
        genreDbStorage.deleteGenre(genre);
        Genre testGenre = genreDbStorage.findById(8L).orElse(null);
        assertThat(testGenre == null).isEqualTo(true);
    }

    @Test
    public void testFindByFilmIdNotEmptyList() {
        List<Genre> genres = genreDbStorage.findByFilmId(3L);
        assertThat(genres.isEmpty()).isEqualTo(false);
        assertThat(genres).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    public void testFindByFilmIdEmptyList() {
        List<Genre> genres = genreDbStorage.findByFilmId(5L);
        assertThat(genres.isEmpty()).isEqualTo(true);
    }

    @Test
    public void testGetAllGenre() {
        Genre genre = genreDbStorage.findById(3L).orElseThrow(() -> new InternalServerException("user not found"));
        Genre genre2 = genreDbStorage.findById(5L).orElseThrow(() -> new InternalServerException("user not found"));

        List<Genre> genres = genreDbStorage.getAllGenre();
        assertThat(genres.isEmpty()).isEqualTo(false);
        assertThat(genres).contains(genre);
        assertThat(genres).contains(genre2);
    }
}