package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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


    public List<User> getCommonFriends(Long first, Long second) {
        return inMemoryUserStorage.getCommonFriends(first, second);
    }

    public List<User> getUsersFriends(Long userId) {
        return inMemoryUserStorage.getFriends(userId);
    }

    public List<User> getAllUsers() {
        return inMemoryUserStorage.findAll().stream().toList();
    }

    public User createUser(User user) {

        // формируем дополнительные данные
        user.setId(inMemoryUserStorage.getNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        // сохраняем нового пользователя в памяти приложения
        return inMemoryUserStorage.addNewUser(user);
    }

    public User changeUsersData(User user) {
        //Оставляем только подготовку данных для внесения изменений. Корректные данные принимаем, вместо некорректных
        //оставляем null, не заглядывая при этом в хранилище. Предполагается, что класс-хранитель заменит ненулевые
        //поля, проверив дополнительно, не заняты ли login и e-mail
        User preparedUser = new User();
        preparedUser.setId(user.getId());
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("\nUsers new invalid login will be not updated {}", user);
        } else {
            preparedUser.setLogin(user.getLogin());
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@") ||
                user.getEmail().indexOf("@") != user.getEmail().lastIndexOf("@")) {
            log.warn("\nUsers new invalid e-mail will be not updated {}", user);
        } else {
            preparedUser.setEmail(user.getEmail());
        }
        try {
            if (!LocalDate.now().isBefore(user.getBirthday())) {
                preparedUser.setBirthday(user.getBirthday());
            } else {
                log.warn("\nUsers new invalid birthday will be not updated {}", user);
            }
        } catch (RuntimeException e) {
            log.warn("\nUsers new invalid birthday will be not updated {}", user);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            preparedUser.setName(user.getLogin());
        } else
            preparedUser.setName(user.getName());
        return inMemoryUserStorage.modifyUser(preparedUser);
    }

    public List<User> makeNewFriendsPair(long l1, long l2) {
        return inMemoryUserStorage.setNewFriendship(l1, l2);
    }

    public List<User> deleteFromFriends(Long first, Long second) {
        return inMemoryUserStorage.deleteFriendship(first, second);
    }

}