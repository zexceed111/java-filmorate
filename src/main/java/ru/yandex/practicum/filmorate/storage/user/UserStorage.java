package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addNewUser(User newUser);

    User deleteUser(User user);

    User modifyUser(User user);

    List<User> setNewFriendship(Long l1, Long l2);

    List<User> deleteFriendship(long l1, long l2);

    List<User> getFriends(long userId);

    List<User> getCommonFriends(long l1, long l2);
}