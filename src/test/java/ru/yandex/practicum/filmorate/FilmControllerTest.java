package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void addFilm_withValidData_shouldReturnCreatedFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film.");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);

        ResponseEntity<Film> response = filmController.addFilm(film);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(film, response.getBody());
    }

    @Test
    void addFilm_withEmptyName_shouldThrowValidationException() {
        Film film = new Film();
        film.setName("");
        film.setDescription("This is a test film.");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void addFilm_withDescriptionLongerThan200Chars_shouldThrowValidationException() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film with a description that is longer than 200 characters. This is a test film with a description that is longer than 200 characters.");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void addFilm_withNegativeDuration_shouldThrowValidationException() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film.");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(-120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void addFilm_withReleaseDateBeforeMinimum_shouldThrowValidationException() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film.");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void updateFilm_withExistingId_shouldUpdateFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film.");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);

        ResponseEntity<Film> addResponse = filmController.addFilm(film);
        long id = addResponse.getBody().getId();

        Film updatedFilm = new Film();
        updatedFilm.setId(id);
        updatedFilm.setName("Updated Test Film");
        updatedFilm.setDescription("This is an updated test film.");
        updatedFilm.setReleaseDate(LocalDate.of(2022, 2, 1));
        updatedFilm.setDuration(150);

        ResponseEntity<Film> updateResponse = filmController.updateFilm(id, updatedFilm);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(updatedFilm, updateResponse.getBody());
    }

    @Test
    void updateFilm_withNonExistentId_shouldReturnNotFound() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film.");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);

        ResponseEntity<Film> response = filmController.updateFilm(1L, film);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllFilms_shouldReturnAllFilms() {
        Film film1 = new Film();
        film1.setName("Test Film 1");
        film1.setDescription("This is test film 1.");
        film1.setReleaseDate(LocalDate.of(2022, 1, 1));
        film1.setDuration(120);

        Film film2 = new Film();
        film2.setName("Test Film 2");
        film2.setDescription("This is test film 2.");
        film2.setReleaseDate(LocalDate.of(2022, 2, 1));
        film2.setDuration(150);

        filmController.addFilm(film1);
        filmController.addFilm(film2);

        ResponseEntity<Collection<Film>> response = filmController.getAllFilms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertTrue(response.getBody().contains(film1));
        assertTrue(response.getBody().contains(film2));
    }
}
