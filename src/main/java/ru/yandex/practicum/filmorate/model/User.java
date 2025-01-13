package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validators.UsersLoginConstraint;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    Long id;

    @Email(message = "Некорректный e-mail пользователя")
    @NotBlank(message = "Некорректный e-mail пользователя")
    String email;

    @UsersLoginConstraint
    String login;

    String name;

    @NotNull(message = "Некорректная дата рождения")
    @PastOrPresent(message = "Некорректная дата рождения")
    LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

}