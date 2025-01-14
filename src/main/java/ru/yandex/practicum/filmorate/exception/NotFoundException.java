package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, User newUser) {
        super(message);
    }

    public NotFoundException(String message, Film newUser) {
        super(message);
    }

    public NotFoundException(String massage, Throwable cause) {
        super(massage, cause);
    }
}
