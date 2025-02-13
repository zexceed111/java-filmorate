package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import ru.yandex.practicum.filmorate.validators.UsersLoginConstraint;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    Long id; // уникальный идентификатор пользователя,

    @Email(message = "Некорректный e-mail пользователя")
    @NotBlank(message = "Некорректный e-mail пользователя") //неожиданно оказалось, что @Email не ловит ""
    String email; //электронная почта пользователя

    @UsersLoginConstraint //Поскольку в задании есть запрет на наличие пробелов в логине, здесь тоже сделал custom
    String login; //логин пользователя

    String name; //имя пользователя для отображения,

    @NotNull(message = "Некорректная дата рождения")
    @PastOrPresent(message = "Некорректная дата рождения")
    LocalDate birthday; //дата рождения

    //Множество id друзей в порядке увеличения id
    Set<Long> friends = new TreeSet<>((l1, l2) -> Math.toIntExact(l1 - l2));

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

}