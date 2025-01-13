package ru.yandex.practicum.filmorate.exception;

public class DuplicateData extends ValidationException {
    public DuplicateData(String message, Object objForBody) {
        super(message, objForBody);
    }
}
