package ru.yandex.practicum.filmorate.exception;

public class DuplicateDataException extends ValidationException {
    public DuplicateDataException(String message, Object objForBody) {
        super(message, objForBody);
    }
}
