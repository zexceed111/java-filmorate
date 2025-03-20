package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserRequest request) {
        log.info("\nCreation user {}", request);
        return userService.createUser(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@Valid @RequestBody UserRequest request) {
        log.info("\nUpdating user {}", request);
        return userService.changeUsersData(request);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> addFriends(@PathVariable @Positive(message = "Users Id must be positive") long id,
                                    @PathVariable @Positive(message = "Users Id must be positive") long friendId) {
        log.info("\nMaking {} as friend {}", id, friendId);
        if (id == friendId) {
            log.warn("\nNot added friends {} and {} because identifiers are equal", id, friendId);
            throw new ValidationException("Friends are not added.");
        }
        return userService.makeNewFriendsPair(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> killFriendship(@PathVariable @Positive(message = "Users Id must be positive") long id,
                                        @PathVariable @Positive(message = "Users Id must be positive") long friendId) {
        log.info("\nDelete {} as friend {}", id, friendId);
        if (id == friendId) {
            log.warn("\nNot deleted friendship between {} and {} because identifiers are equal", id, friendId);
            throw new ValidationException("Friends are not deleted.");
        }
        return userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsersFriends(@PathVariable @Positive(message = "Users Id must be positive") Long id) {
        log.info("\nGetting friendslist of {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getCommonFriends(@PathVariable @Positive(message = "Users Id must be positive") long id,
                                          @PathVariable @Positive(message = "Users Id must be positive") long otherId) {
        log.info("\nGetting common friends {} and {}", id, otherId);
        if (id == otherId) {
            log.warn("\nList common friends {} and {} is impossible because identifiers are equal", id, otherId);
            throw new ValidationException("Friends are not found.");
        }
        return userService.getCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto deleteUser(@PathVariable @Positive(message = "Users Id must be positive") long id) {
        return userService.deleteUser(id);
    }

}