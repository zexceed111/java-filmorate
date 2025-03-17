package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RatingRequest {
    private final Long id;

    @NotBlank(message = "Признак MPA-рейтинга не может быть пустым.")
    @Size(message = "Слишком длинное наименование рейтинга (более 7 символов)", max = 7)
    private String name;

    @Size(message = "Слишком длинное описание рейтинга (более 200 символов)", max = 200)
    private String description;
}