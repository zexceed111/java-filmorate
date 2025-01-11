package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class UserControllerTest {

    private UserController userController;
    private Map<Long, User> users;

    @BeforeEach
    void setUp() {
        users = new HashMap<>();
        userController = new UserController();
    }

    @Test
    void createUser_ValidData_ReturnsCreatedUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        ResponseEntity<User> response = userController.createUser(user);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());
        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void createUser_InvalidEmail_ThrowsValidationException() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUser_InvalidLogin_ThrowsValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("invalid login");
        user.setName("Test User");
        user.setBirthDate(LocalDate.of(1990, 1, 1));

        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void createUser_FutureBirthDate_ThrowsValidationException() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthDate(LocalDate.now().plusDays(1));

        Assertions.assertThrows(ValidationException.class, () -> userController.createUser(user));
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void updateUser_ExistingUser_ReturnsUpdatedUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        userController.createUser(user);

        User updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updateduser");
        updatedUser.setName("Updated User");
        updatedUser.setBirthDate(LocalDate.of(1985, 5, 5));

        ResponseEntity<User> response = userController.updateUser(user.getId(), updatedUser);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(updatedUser, response.getBody());
        Assertions.assertEquals(1, userController.getUsers().size());
    }

    @Test
    void updateUser_NonExistentUser_ReturnsNotFound() {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updateduser");
        updatedUser.setName("Updated User");
        updatedUser.setBirthDate(LocalDate.of(1985, 5, 5));

        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void getUser_ExistingUser_ReturnsUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        userController.createUser(user);

        ResponseEntity<User> response = userController.getUser(user.getId());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    void getUser_NonExistentUser_ReturnsNotFound() {
        ResponseEntity<User> response = userController.getUser(1L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteUser_ExistingUser_ReturnsNoContent() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        userController.createUser(user);

        ResponseEntity<Void> response = userController.deleteUser(user.getId());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertEquals(0, userController.getUsers().size());
    }

    @Test
    void deleteUser_NonExistentUser_ReturnsNotFound() {
        ResponseEntity<Void> response = userController.deleteUser(1L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

