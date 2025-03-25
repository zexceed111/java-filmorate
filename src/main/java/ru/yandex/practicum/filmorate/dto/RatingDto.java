package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RatingDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    private String name;
    private String description;
}
