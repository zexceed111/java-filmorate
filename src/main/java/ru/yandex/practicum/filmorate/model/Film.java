package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validators.ReleaseDateConstraint;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

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
    Long duration; //пока предполагаем продолжительность в минутах

    //Множество id лайкнувших юзеров в порядке увеличения id
    Set<Long> usersLikes = new TreeSet<>((l1, l2) -> Math.toIntExact(l1 - l2));

    public Film(String name, String description, LocalDate localDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = localDate;
        this.duration = duration;
    }

    public Film(Long id, String name, String description, LocalDate localDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = localDate;
        this.duration = duration;
    }
}