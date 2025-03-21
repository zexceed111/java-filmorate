package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.dal.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreFilmDbFilmoRateApplicationTests {

    private final FilmGenreDbStorage filmGenreDbStorage;

    @Test
    public void testGetGenresOfFilmNotEmptyList() {
        List<FilmGenre> genres = filmGenreDbStorage.getGenresOfFilm(3L);
        assertThat(genres.isEmpty()).isEqualTo(false);
        assertThat(genres).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    public void testGetGenresOfFilmEmptyList() {
        List<FilmGenre> genres = filmGenreDbStorage.getGenresOfFilm(5L);
        assertThat(genres.isEmpty()).isEqualTo(true);
    }

    @Test
    public void testAddFilmGenresAndDeleting() {
        Long filmId = 6L;
        List<Long> newGenres = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L));
        List<FilmGenre> genresFilm6 = filmGenreDbStorage.getGenresOfFilm(filmId);
        assertThat(genresFilm6.isEmpty()).isEqualTo(true);
        filmGenreDbStorage.addFilmGenres(6L, newGenres);
        genresFilm6 = filmGenreDbStorage.getGenresOfFilm(filmId);
        assertThat(genresFilm6.isEmpty()).isEqualTo(false);
        assertThat(genresFilm6).hasSize(4);
        //Genres for film were added
        filmGenreDbStorage.deleteGenreOfFilms(filmId);
        //Удаляем жанры фильма. Теперь его список снова пуст
        genresFilm6 = filmGenreDbStorage.getGenresOfFilm(filmId);
        assertThat(genresFilm6.isEmpty()).isEqualTo(true);
    }

}