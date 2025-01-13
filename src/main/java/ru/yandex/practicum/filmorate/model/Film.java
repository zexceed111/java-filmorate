package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validators.ReleaseDateConstraint;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    String name;

    @Size(message = "Слишком длинное описание фильма (более 200 символов)", max = 200)
    String description;

    @ReleaseDateConstraint(message = "Некорректная дата релиза фильма")
    LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма должна быть положительным числом")
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    Long duration;

    public Film(String name, String description, LocalDate localDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = localDate;
        this.duration = duration;
    }

}