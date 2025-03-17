package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User createUser(User user);

    User deleteUser(User user);

    User modifyUser(User user);

    User setNewFriendship(Long l1, Long l2);

    List<User> deleteFriendship(long l1, long l2);

    List<User> getFriends(long userId);

    List<User> getCommonFriends(long l1, long l2);

    List<User> getAllUsers();

    Optional<User> findById(long userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);

    boolean isFriendPairExist(long l1, long l2);

}