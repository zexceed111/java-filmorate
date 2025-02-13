package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Измененные в связи с добавлением UserService методы
    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping
    public @ResponseBody User create(@Valid @RequestBody User user) {
        log.info("\nCreation user {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public @ResponseBody User update(@Valid @RequestBody User renewedUser) {
        log.info("Updating user {}", renewedUser);
        return userService.changeUsersData(renewedUser);
    }

    //Добавленные методы
    @PutMapping("/{id}/friends/{friendId}")
    public List<User> addFriends(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        log.info("\nMaking {} as friend {}", id, friendId);
        if (id == friendId) {
            log.warn("\nNot added friends {} and {} because identifiers are equal", id, friendId);
            throw new ValidationException("Friends are not added.", "Identifiers have not be equal.");
        }
        return userService.makeNewFriendsPair(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> killFriendship(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        log.info("\nDelete {} as friend {}", id, friendId);
        if (id == friendId) {
            log.warn("\nNot deleted friendship between {} and {} because identifiers are equal", id, friendId);
            throw new ValidationException("Friends are not deleted.", "Identifiers have not be equal.");
        }
        return userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUsersFriends(@PathVariable @Positive long id) {
        log.info("\nGetting friendslist of {}", id);
        return userService.getUsersFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListCommonFriends(@PathVariable @Positive long id, @PathVariable @Positive long otherId) {
        log.info("\nGetting common friends {} and {}", id, otherId);
        if (id == otherId) {
            log.warn("\nNot deleted friendship between {} and {} because identifiers are equal", id, otherId);
            throw new ValidationException("Friends are not deleted.", "Identifiers have not be equal.");
        }
        return userService.getCommonFriends(id, otherId);
    }

}