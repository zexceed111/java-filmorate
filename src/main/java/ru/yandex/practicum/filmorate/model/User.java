package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Data
public class User {
    private long id;
    @NotBlank(message = "Электронная почта не может быть пустой и должна содержать символ '@'.")
    @Email(message = "Электронная почта должна быть корректной.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым и не может содержать пробелы.")
    private String login;
    private String name;
    @NotNull(message = "Дата рождения не может быть пустой.")
    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthDate;
}
