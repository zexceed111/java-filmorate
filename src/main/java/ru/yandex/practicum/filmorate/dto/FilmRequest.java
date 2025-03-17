package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.validators.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilmRequest {

    @Positive(message = "Id фильма должен быть положительным числом")
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    @Size(message = "Слишком длинное название фильма (более 200 символов)", max = 200)
    private String name;

    @Size(message = "Слишком длинное описание фильма (более 200 символов)", max = 200)
    private String description;

    @ReleaseDateConstraint(message = "Некорректная дата релиза фильма")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма должна быть положительным числом")
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Long duration; //пока предполагаем продолжительность в минутах

    @NotNull(message = "Рейтинг МПА должен быть указан")
    private Rating mpa;

    private List<Genre> genres; //Для получения списка жанров

    public FilmRequest(String name, String description, LocalDate releaseDate, Long duration, Rating mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}