package ru.yandex.practicum.filmorate.exception;

public class DuplicateData extends RuntimeException {

    public DuplicateData(String message, Object objForBody) {
        super(message, (Throwable) objForBody);
    }
}
