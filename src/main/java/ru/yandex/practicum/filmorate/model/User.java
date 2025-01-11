package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private int birthday;
}
