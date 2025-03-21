package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbFilmoRateApplicationTests {

    private final FilmDbStorage filmStorage;

    private final FilmRequest standartFilm = new FilmRequest("Halloween", "Classic horror",
            LocalDate.of(1978, 10, 25), 101L,
            new Rating(5L, null, null));

    @Test
    public void testFindFilmById() {

        Optional<Film> filmOptional = filmStorage.findById(1);

        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindFilmByName() {

        Optional<Film> filmOptional = filmStorage.findByName("Экипаж");

        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 9L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "Экипаж");
                        }
                );
    }

    @Test
    public void testCreateFilm() {

        FilmRequest request = standartFilm;
        Film film = filmStorage.addNewFilm(request);
        Film createdFilm = filmStorage.findById(film.getId()).orElse(null);

        assertThat(createdFilm == null).isEqualTo(false);
        assertThat(createdFilm).hasFieldOrPropertyWithValue("name", request.getName());
        assertThat(createdFilm).hasFieldOrPropertyWithValue("description", request.getDescription());
        assertThat(createdFilm).hasFieldOrPropertyWithValue("releaseDate", request.getReleaseDate());
        assertThat(createdFilm).hasFieldOrPropertyWithValue("duration", request.getDuration());
        assertThat(createdFilm.getMpaId()).isEqualTo(request.getMpa().getId());
    }

    @Test
    public void testChangeFilm() {
        FilmRequest request = standartFilm;
        Film film = FilmMapper.mapToFilm(request);
        film.setId(1L);

        filmStorage.changeFilm(film);
        Film testedFilm = filmStorage.findById(1L)
                .orElseThrow(() -> new InternalServerException("testedFilm not found"));
        assertThat(testedFilm).hasFieldOrPropertyWithValue("name", request.getName());
        assertThat(testedFilm).hasFieldOrPropertyWithValue("description", request.getDescription());
        assertThat(testedFilm).hasFieldOrPropertyWithValue("releaseDate", request.getReleaseDate());
        assertThat(testedFilm).hasFieldOrPropertyWithValue("duration", request.getDuration());
        assertThat(testedFilm.getMpaId()).isEqualTo(request.getMpa().getId());
    }

    @Test
    public void testDeleteFilm() {
        Film film = filmStorage.findById(8L).orElseThrow(() -> new InternalServerException("film not found"));
        filmStorage.deleteFilm(film);
        Film testFilm = filmStorage.findById(8L).orElse(null);
        assertThat(testFilm == null).isEqualTo(true);
    }

    @Test
    public void testGetAllFilms() {
        Film film = filmStorage.findById(3L).orElseThrow(() -> new InternalServerException("film not found"));
        Film film2 = filmStorage.findById(5L).orElseThrow(() -> new InternalServerException("film not found"));

        List<Film> films;
        films = filmStorage.getAll();
        assertThat(films.isEmpty()).isEqualTo(false);
        assertThat(films).contains(film);
        assertThat(films).contains(film2);
    }

    @Test
    public void testGetPopular() {

        List<Film> films;
        films = filmStorage.getPopular(5L);
        assertThat(films == null).isEqualTo(false);
        assertThat(films).hasSize(5);
        assertThat(films.get(1).getCount()).isGreaterThanOrEqualTo(films.get(2).getCount());
    }

    @Test
    public void testFindFilmWithLike() {
        boolean isLikeExists = filmStorage.findFilmWithLike(5L, 5L).isPresent();
        assertThat(isLikeExists).isEqualTo(false);
        isLikeExists = filmStorage.findFilmWithLike(3L, 3L).isPresent();
        assertThat(isLikeExists).isEqualTo(true);
    }

    @Test
    public void testAddAndDeleteLike() {
        //Вначале фильм нелайкнутый
        boolean isLikeExists = filmStorage.findFilmWithLike(5L, 5L).isPresent();
        assertThat(isLikeExists).isEqualTo(false);
        //Добавляем лайк и проверяем
        filmStorage.addLike(5L, 5L);
        isLikeExists = filmStorage.findFilmWithLike(5L, 5L).isPresent();
        assertThat(isLikeExists).isEqualTo(true);
        //Удаляем лайк и снова проверяем
        filmStorage.deleteLike(5L, 5L);
        isLikeExists = filmStorage.findFilmWithLike(5L, 5L).isPresent();
        assertThat(isLikeExists).isEqualTo(false);
    }

}