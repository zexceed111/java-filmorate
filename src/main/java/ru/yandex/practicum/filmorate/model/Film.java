package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Film.
 */

@Getter
@Setter
@Data
public class Film {
    private long id;
    private String name;
    private String description;
    private int releaseDate;
    private int duration;
}
