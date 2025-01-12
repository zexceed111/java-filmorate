package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
//
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    public User(int id, String email, String login, String name, LocalDate birthDate) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthDate = birthDate;
    }


    public void validateBirthDate() {
        if (birthDate == null) {
            throw new IllegalArgumentException("Поле birthDate не может быть пустым.");
        }
        try {
            LocalDate.parse(birthDate.format(DateTimeFormatter.ISO_DATE));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты рождения. Используйте ISO-формат (yyyy-MM-dd).");
        }
    }
}
