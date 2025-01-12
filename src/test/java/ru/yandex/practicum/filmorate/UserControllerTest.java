package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class UserControllerTest {

    private final UserController userController = new UserController(); //Создаем тестовый контроллер

    @Test
    public void createUser_validUser_success() {
        User user = new User(1, "test@example.com", "testuser", "Kostya", LocalDate.of(2000, 1, 1));
        ResponseEntity<User> response = userController.createUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void createUser_nullUser_throwsValidationException() {
        assertThrows(ValidationException.class, () -> userController.createUser(null));
    }

    @Test
    public void createUser_emptyLogin_throwsValidationException() {
        User user = new User(1, "test@example.com", "", "kostya", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }


    @Test
    public void createUser_invalidEmail_throwsValidationException() {
        User user = new User(1, "", "testuser", "Kostya", LocalDate.of(2000, 1, 1));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }


    @Test
    public void createUser_futureBirthDate_throwsValidationException() {
        User user = new User(1, "test@example.com", "testuser", "kostya", LocalDate.now().plusYears(1));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void getUser_existingUser_success() {
        // Инициализируйте тестовый пользователь в контроллере
        User user = new User(1, "test@example.com", "testuser", "kostya", LocalDate.of(2000, 1, 1));
        userController.createUser(user);

        ResponseEntity<User> response = userController.getUser(user.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    public void getUser_nonExistingUser_notFound() {
        ResponseEntity<User> response = userController.getUser(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void updateUser_existingUser_success() {
        User user = new User(1, "test@example.com", "testuser", "kostya", LocalDate.of(2000, 1, 1));
        userController.createUser(user);
        User updatedUser = new User(1, "updatedEmail@example.com", "UpDatetestuser", "kostya", LocalDate.of(2001, 2, 3));
        updatedUser.setId(user.getId());
        ResponseEntity<User> response = userController.updateUser(user.getId(), updatedUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void updateUser_nonExistingUser_notFound() {
        User updatedUser = new User(3, "updatedEmail@example.com", "UpDatetestuser", "kostya", LocalDate.of(2001, 2, 3));
        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUser_existingUser_success() {
        User user = new User(1, "test@example.com", "testuser", "kostya", LocalDate.of(2000, 1, 1));
        userController.createUser(user);
        ResponseEntity<Void> response = userController.deleteUser(user.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteUser_nonExistingUser_notFound() {
        ResponseEntity<Void> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}