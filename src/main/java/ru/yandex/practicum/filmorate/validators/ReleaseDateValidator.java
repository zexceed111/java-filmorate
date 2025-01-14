package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    private final LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);

    @Override
    public void initialize(ReleaseDateConstraint releaseDateChecking) {

    }

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext cxt) {
        System.out.println(releaseDate);
        if (releaseDate == null) {
            return false;
        }
        return !releaseDate.isBefore(cinemaBirthday);
    }
}
