package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validators.UsersLoginConstraint;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserRequest {

    @Positive(message = "Users id must be positive")
    private Long id;

    //Поскольку контроллер теперь принимает userRequest, аннотации перенес в класс UserRequest
    @Email(message = "Некорректный e-mail пользователя")
    @NotBlank(message = "Некорректный e-mail пользователя")
    @Size(message = "Слишком длинный e-mail (более 50 символов)", max = 50)
    private String email;

    @UsersLoginConstraint
    @Size(message = "Слишком длинный логин (более 15 символов)", max = 15)
    private String login;

    @Size(message = "Слишком длинное имя пользователя (более 200 символов)", max = 200)
    private String name;

    @NotNull(message = "Некорректная дата рождения")
    @PastOrPresent(message = "Некорректная дата рождения")
    private LocalDate birthday;

    public UserRequest(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public boolean hasName() {
        return !((name == null) || (name.isBlank()));
    }
}
