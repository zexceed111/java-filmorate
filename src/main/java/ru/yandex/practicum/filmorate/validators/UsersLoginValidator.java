package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsersLoginValidator implements
        ConstraintValidator<UsersLoginConstraint, String> {

    @Override
    public void initialize(UsersLoginConstraint login) {

    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext cxt) {
        if (login == null) {
            return false;
        } else {
            return !login.isEmpty() && !login.contains(" ");
        }
    }
}