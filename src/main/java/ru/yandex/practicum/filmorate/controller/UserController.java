package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Map<Long, User> users = new HashMap<>();

    private long getNextId() {
        long currentMaxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Запрос на создание пользователя: {}", user);
        try {
            if (user.getEmail() == null || !user.getEmail().contains("@")) {
                log.error("Валидация не пройдена: Электронная почта не может быть пустой и должна содержать символ '@'.");
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ '@'.");
            }

            if (user.getLogin() == null || user.getLogin().trim().isEmpty() || user.getLogin().contains(" ")) {
                log.error("Валидация ен пройдена: Логин не может быть пустым и не может содержать пробелы.");
                throw new ValidationException("Логин не может быть пустым и не может содержать пробелы.");
            }

            //тут я не понимаю как добавить обработку исключения, если дата рождения будет в будущем
            // у меня валидация past стоит и когда делаю проверку ругается, что тип не тот(принимает только true и false)
        /*

        if (user.getBirthDate() == null || user.getBirthDate().after(new Date())) {
        throw new ValidationException("Дата рождения не может быть в будущем.");
        }

         */

            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.info("Имя пользователя {}", user.getName());
            }
            user.setId(getNextId());
            log.info("Пользователь добавлен: {}", user);
            users.put(user.getId(), user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ValidationException e) {
            log.error("Ошибка при добавлении пользователся: {}", e.getMessage());
            throw e;
        }
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
        User user = users.get(id);
        if (user == null) {
            log.warn("Пользователь с ID {} не найден", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Пользователь найден: {}", user);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
