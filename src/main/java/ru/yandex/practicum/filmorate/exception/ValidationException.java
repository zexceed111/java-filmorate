package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    Object objForBody;

    public ValidationException(String message, Object objForBody) {
        super(message);
        this.objForBody = objForBody;
    }

}