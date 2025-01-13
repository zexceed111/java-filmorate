package ru.yandex.practicum.filmorate.exception;

public class DuplicateDataException extends RuntimeException {

    public DuplicateDataException(String message, Object objForBody) {
        super(message, (Throwable) objForBody);
    }
}
