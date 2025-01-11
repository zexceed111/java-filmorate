package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

/**
 * Film.
 */

@Getter
@Setter
@Data
public class Film {
    private long id;
    @NotBlank(message = "название фильма не может быть пустым.")
    private String name;
    @NotBlank(message = "максимальная длина описания — 200 символов")
    private String description;
    @Past(message = "дата релиза фильма — не раньше 28 декабря 1895 года")
    private Object releaseDate;
    @NotBlank(message = "продолжительность фильма должна быть положительным числом.")
    private int duration;

}
