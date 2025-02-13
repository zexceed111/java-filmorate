package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addNewUser(User newUser) throws DuplicateDataException;

    User deleteUser(User user);

    User modifyUser(User user) throws NotFoundException, DuplicateDataException;

    List<User> setNewFriendship(Long l1, Long l2) throws NotFoundException;

    List<User> deleteFriendship(long l1, long l2) throws NotFoundException;

    List<User> getFriends(long userId) throws NotFoundException;

    List<User> getCommonFriends(long l1, long l2) throws NotFoundException;
}