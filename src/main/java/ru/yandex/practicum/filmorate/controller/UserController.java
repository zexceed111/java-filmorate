package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    private long getNextId() {
        long currentMaxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }


    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Запрос на создание пользователя: {}", user);

        if (user == null) {
            throw new ValidationException("Запрос не содержит данные пользователя.");
        }
        try {
            user.validateBirthDate();
            user.setId(getNextId());
            users.put(user.getId(), user);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage());
        }

        log.info("Пользователь добавлен: {}", user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        log.info("Запрос на обновление пользователя с ID {}: {}", id, updatedUser);
        if (!users.containsKey(id)) {
            log.warn("Пользователь с ID {} не найден для обновления", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        updatedUser.setId(id);
        users.put(id, updatedUser);
        log.info("Пользователь успешно обновлен: {}", updatedUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.info("Запрос на получение пользователя с ID: {}", id);
        Optional<User> optionalUser = Optional.ofNullable(users.get(id));
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя с ID: {}", id);
        if (!users.containsKey(id)) {
            log.warn("Пользователь с ID {} не найден для удаления", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        users.remove(id);
        log.info("Пользователь с ID {} успешно удален", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
