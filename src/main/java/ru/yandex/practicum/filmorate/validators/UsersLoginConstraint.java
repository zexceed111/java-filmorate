package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsersLoginValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface UsersLoginConstraint {
    String message() default "Login не указан или содержит пробелы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
