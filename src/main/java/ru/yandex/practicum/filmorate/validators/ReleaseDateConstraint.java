package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReleaseDateValidator.class)
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface ReleaseDateConstraint {
    String message() default "Некорректная дата релиза фильма";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}