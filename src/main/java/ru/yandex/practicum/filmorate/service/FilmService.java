package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final RatingStorage ratingStorage;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage, @Qualifier("filmGenreDbStorage") FilmGenreStorage filmGenreStorage, @Qualifier("ratingDbStorage") RatingStorage ratingStorage, GenreDbStorage genreDbStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.ratingStorage = ratingStorage;
        this.genreDbStorage = genreDbStorage;
    }

    public FilmDto addNewFilm(FilmRequest request) {
        //Больше подходит NotFound, но тест Postman ожидает ошибку 400
        Rating mpa = ratingStorage.findById(request.getMpa().getId()).orElseThrow(() -> new ValidationException("Указан несуществующий рейтинг МПА"));
        Set<Genre> genreSet = new HashSet<>();
        if (request.getGenres() != null) {
            for (Genre g : request.getGenres()) {
                genreSet.add(genreDbStorage.findById(g.getId()).orElseThrow(() -> new ValidationException("Ошибочный id жанра")));
            }
        }
        log.warn("\nSet of Long {}", genreSet);
        Film film = filmStorage.addNewFilm(request);
        filmGenreStorage.addFilmGenres(film.getId(), genreSet.stream().map(Genre::getId).toList());
        List<Genre> genres = genreDbStorage.findByFilmId(film.getId());
        return FilmMapper.mapToFilmDtoWithGenre(film, mpa, genres);
    }

    public FilmDto modifyFilm(FilmRequest request) {
        Long id = request.getId();
        log.warn("Value of {}", id);

        // Проверка существования фильма
        Film existingFilm = filmStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Film " + id + " not found"));

        // Проверка рейтинга MPA
        if (request.getMpa() == null || request.getMpa().getId() == null) {
            throw new ValidationException("Рейтинг MPA обязателен");
        }
        Rating mpa = ratingStorage.findById(request.getMpa().getId())
                .orElseThrow(() -> new ValidationException("Указан несуществующий рейтинг MPA"));

        // Создаём объект фильма
        Film film = FilmMapper.mapToFilm(request);
        film.setId(id);

        // Обновляем жанры
        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            filmGenreStorage.deleteGenreOfFilms(id);
            Set<Genre> genreSet = new HashSet<>(request.getGenres());
            filmGenreStorage.addFilmGenres(film.getId(), genreSet.stream().map(Genre::getId).toList());
        }

        // Получаем обновлённые жанры
        List<Genre> genres = genreDbStorage.findByFilmId(film.getId());

        return FilmMapper.mapToFilmDtoWithGenre(filmStorage.changeFilm(film), mpa, genres);
    }


    public FilmDto deleteFilm(Long filmId) {
        Film film = filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException("Not found film with id= " + filmId, filmId));
        Rating mpa = ratingStorage.findById(film.getMpaId()).orElseThrow(() -> new InternalServerException("Не удалось прочитать МПА"));
        return FilmMapper.mapToFilmDto(filmStorage.deleteFilm(film), mpa);
    }

    public List<FilmDto> getAll() {
        return filmStorage.getAll().stream().map(film -> FilmMapper.mapToFilmDto(film, ratingStorage.findById(film.getMpaId()).orElseThrow(() -> new InternalServerException("Не удалось прочитать МПА")))).toList();
    }

    public FilmDto getFilm(long id) {
        Film film = filmStorage.findById(id).orElseThrow(() -> new NotFoundException("Film id " + id + " not found", id));
        log.warn("Found {}", film);
        Rating mpa = ratingStorage.findById(film.getMpaId()).orElseThrow(() -> new InternalServerException("Не удалось прочитать МПА"));
        List<Genre> genres = genreDbStorage.findByFilmId(film.getId());
        return FilmMapper.mapToFilmDtoWithGenre(film, mpa, genres);
    }

    public List<FilmDto> getPopularFilms(long count) {
        return filmStorage.getPopular(count).stream().map(film -> FilmMapper.mapToFilmDto(film, ratingStorage.findById(film.getMpaId()).orElseThrow(() -> new InternalServerException("Не удалось прочитать МПА")))).toList();

    }

    public FilmDto addUsersLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException("Film " + filmId + " not found", filmId));
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("User id = " + userId + " not exist", userId));
        if (filmStorage.findFilmWithLike(filmId, userId).isPresent()) {
            log.warn("\nLike of film {} by user {} already exists", filmId, userId);
            throw new DuplicateDataException("Like of film " + filmId + " by user " + userId + " already exists.", filmId);
        }
        Rating mpa = ratingStorage.findById(film.getMpaId()).orElseThrow(() -> new NotFoundException("Рейтинг MPA с id " + film.getMpaId() + " не найден."));
        return FilmMapper.mapToFilmDto(filmStorage.addLike(filmId, userId), mpa);
    }

    public FilmDto deleteUsersLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException("Film " + filmId + " not found", filmId));
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("User id = " + userId + " not exist", userId));
        if (filmStorage.findFilmWithLike(filmId, userId).isEmpty()) {
            log.warn("\nLike of film {} by user {} not exists", filmId, userId);
            throw new NotFoundException("Like of film " + filmId + " by user " + userId + " not exists.", filmId);
        }
        Rating mpa = ratingStorage.findById(film.getMpaId()).orElseThrow(() -> new NotFoundException("Не удалось прочитать МПА"));
        return FilmMapper.mapToFilmDto(filmStorage.deleteLike(filmId, userId), mpa);
    }

}