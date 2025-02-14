package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void makeNewFriendsPair(Long first, Long second) {

    }


    public List<User> getCommonFriends(Long first, Long second) throws NotFoundException {
        return inMemoryUserStorage.getCommonFriends(first, second);
    }

    public List<User> getUsersFriends(Long userId) throws NotFoundException {
        return inMemoryUserStorage.getFriends(userId);
    }

    public List<User> getAllUsers() {
        return inMemoryUserStorage.findAll().stream().toList();
    }

    public User createUser(User user) throws DuplicateDataException {

        // формируем дополнительные данные
        user.setId(inMemoryUserStorage.getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        // сохраняем нового пользователя в памяти приложения
        return inMemoryUserStorage.addNewUser(user);
    }

    public User changeUsersData(User user) throws ValidationException {
        if (user.getId() == null) {
            throw new ValidationException("Id пользователя должен быть указан", user);
        }

        User preparedUser = new User();
        preparedUser.setId(user.getId());

        if (user.getLogin() != null && !user.getLogin().isBlank() && !user.getLogin().contains(" ")) {
            preparedUser.setLogin(user.getLogin());
        } else {
            log.warn("Invalid login, not updating: {}", user);
        }

        if (user.getEmail() != null && user.getEmail().contains("@")) {
            preparedUser.setEmail(user.getEmail());
        } else {
            log.warn("Invalid email, not updating: {}", user);
        }

        if (user.getBirthday() != null && !LocalDate.now().isBefore(user.getBirthday())) {
            preparedUser.setBirthday(user.getBirthday());
        }

        if (user.getName() != null && !user.getName().isBlank()) {
            preparedUser.setName(user.getName());
        } else {
            preparedUser.setName(user.getLogin());
        }

        log.info("Successfully updated user: {}", preparedUser);
        return inMemoryUserStorage.modifyUser(preparedUser);
    }

    public List<User> makeNewFriendsPair(long l1, long l2) throws NotFoundException {
        return inMemoryUserStorage.setNewFriendship(l1, l2);
    }

    public List<User> deleteFromFriends(Long first, Long second) throws NotFoundException {
        return inMemoryUserStorage.deleteFriendship(first, second);
    }

}