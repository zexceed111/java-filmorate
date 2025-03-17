package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Getter
@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public List<User> getFriends(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User with id= " + userId + " not found", userId);
        }
        return users.get(userId).getFriends().stream()
                .map(users::get)
                .toList();
    }

    @Override
    public List<User> getCommonFriends(long l1, long l2) {
        if (!users.containsKey(l1)) {
            throw new NotFoundException("User with id= " + l1 + " not found", l1);
        }
        if (!users.containsKey(l2)) {
            throw new NotFoundException("User with id= " + l2 + " not found", l2);
        }

        return users.get(l1).getFriends().stream()
                .filter(l -> users.get(l2).getFriends().contains(l))
                .map(users::get)
                .toList();
    }

    @Override
    public User createUser(User newUser) {
        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        log.info("\nSuccessfully created {}", newUser);
        return newUser;
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    @Override
    public User modifyUser(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("\nNot updated {}", user);
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден", user.getId());
        }
        User oldUser = users.get(user.getId());
        if (user.getLogin() != null) {
            if (!oldUser.getLogin().equals(user.getLogin()) && isUsedLogin(user.getLogin())) {
                log.warn("\nNot updated {}", user);
                throw new DuplicateDataException("Login " + user.getLogin() + " уже используется", user.getLogin());
            } else {
                oldUser.setLogin(user.getLogin());
            }
        }
        if (user.getEmail() != null) {
            if (!oldUser.getEmail().equals(user.getEmail()) && isUsedEmail(user.getEmail())) {
                log.warn("\nNot updated {}", user);
                throw new DuplicateDataException("E-mail " + user.getEmail() + " уже используется", user.getEmail());
            } else {
                oldUser.setEmail(user.getEmail());
            }
        }
        if (user.getName() != null)
            oldUser.setName(user.getName());
        if (user.getBirthday() != null)
            oldUser.setBirthday(user.getBirthday());
        //корректные данные занесены в oldUser
        users.put(oldUser.getId(), oldUser);
        log.info("\nSuccessfully updated {}.", user);
        return oldUser;
    }

    @Override
    public User setNewFriendship(Long l1, Long l2) {
        if (!users.containsKey(l1)) {
            throw new NotFoundException("Not found user id= ", l1);
        }
        if (!users.containsKey(l2)) {
            throw new NotFoundException("Not found user id= ", l2);
        }
        User user = new User();
        user = users.get(l1);
        Set<Long> tmpFriends1 = user.getFriends();
        if (!tmpFriends1.contains(l2)) {
            tmpFriends1.add(l2);
            user.setFriends(tmpFriends1);
            users.put(l1, user);
            log.info("\nSuccessfully updated friend {}.", user);
            return users.get(l2);
        } else
            throw new DuplicateDataException("Пользователь " + l2 + "уже является другом пользователя " + l1,
                    users.get(l2));
    }

    @Override
    public List<User> deleteFriendship(long l1, long l2) {
        if (!users.containsKey(l1)) {
            throw new NotFoundException("Not found user id= ", l1);
        }
        if (!users.containsKey(l2)) {
            throw new NotFoundException("Not found user id= ", l2);
        }
        User user1 = new User();
        user1 = users.get(l1);
        Set<Long> tmpFriends1 = user1.getFriends();
        tmpFriends1.remove(l2);
        user1.setFriends(tmpFriends1);
        users.put(l1, user1);
        User user2 = new User();
        user2 = users.get(l2);
        Set<Long> tmpFriends2 = user2.getFriends();
        tmpFriends2.remove(l1);
        user2.setFriends(tmpFriends2);
        users.put(l2, user2);
        return getFriends(l1);
    }

    //Далее вспомогательные методы
    public boolean isUsedLogin(String login) {
        //Метод проверяет, не занят ли логин другим пользователем
        return users.keySet()
                .stream()
                .map(users::get)
                .anyMatch(user -> user.getLogin().equals(login));
    }

    public boolean isUsedEmail(String email) {
        //Метод проверяет, не занят ли e-mail другим пользователем
        return users.keySet()
                .stream()
                .map(users::get)
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    // Имплементация методов, добавленных в UserStorage
    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.keySet().stream()
                .map(users::get)
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.keySet().stream()
                .map(users::get)
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public boolean isFriendPairExist(long l1, long l2) {
        if (!users.containsKey(l1))
            return false;
        else
            return users.get(l1).getFriends().contains(l2);
    }

}