package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserValidationTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testValidUser() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Пользователь должен быть валидным");
    }

    @Test
    public void testEmptyName() {
        User user = new User();
        user.setName("");
        user.setEmail("john.doe@example.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должно быть одно нарушение валидации");
        assertEquals("Имя не должно быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyEmail() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("");  // Пустой email

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должно быть одно нарушение валидации");
        assertEquals("Email не должен быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    public void testInvalidEmail() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("invalid-email");  // Некорректный email

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должно быть одно нарушение валидации");
        assertEquals("Некорректный адрес электронной почты", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmptyUser() {
        User user = new User();  // Все поля пустые

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(2, violations.size(), "Должно быть два нарушения валидации");
    }
}
