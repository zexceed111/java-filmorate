package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends ValidationException {
    public NotFoundException(String message, Object objForBody) {
        super(message, objForBody);
    }
}
