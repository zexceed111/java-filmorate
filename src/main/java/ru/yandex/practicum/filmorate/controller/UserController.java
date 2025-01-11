package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Setter
    @Getter
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
                log.error("Валидация не пройдена: Логин не может быть пустым и не может содержать пробелы.");
                throw new ValidationException("Логин не может быть пустым и не может содержать пробелы.");
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birthDate = sdf.parse(String.valueOf(user.getBirthDate()));
                if (birthDate.after(new Date())) {
                    throw new ValidationException("Дата рождения не может быть в будущем.");
                }
            } catch (ParseException e) {
                throw new ValidationException("Неверный формат даты рождения.");
            }


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
