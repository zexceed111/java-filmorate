package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GenreRequest {

    @NotBlank(message = "Признак жанра не может быть пустым.")
    @Size(message = "Слишком длинное наименование жанра (более 25 символов)", max = 25)
    private String name;

    @Size(message = "Слишком длинное описание жанра (более 200 символов)", max = 200)
    private String description;
}
