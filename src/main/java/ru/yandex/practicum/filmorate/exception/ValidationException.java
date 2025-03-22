package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final Object objForBody;

    public ValidationException(String message, Object objForBody) {
        super(message);
        this.objForBody = objForBody;
    }

    public ValidationException(String message) {
        super(message);
        this.objForBody = null;
    }
}