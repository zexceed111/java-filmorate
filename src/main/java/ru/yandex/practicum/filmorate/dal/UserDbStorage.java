package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Component("userDbStorage")
public class UserDbStorage extends BaseRepository<User> implements UserStorage {

    private static final String INSERT_USER_QUERY = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users ORDER BY id";
    private static final String GET_FRIENDS_QUERY = "SELECT * FROM users AS u WHERE u.id IN (" + "SELECT user2_id FROM friends f WHERE user1_id = ?)";

    private static final String GET_COMMON_FRIENDS_QUERY = "SELECT * FROM users AS u WHERE u.id IN " + "(SELECT user2_id FROM friends f WHERE user1_id = ?) AND u.id IN" + "(SELECT user2_id FROM friends f2 WHERE user1_id = ?) AND u.id NOT IN (?, ?)";
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friends WHERE user1_id = ? AND user2_id = ?";
    private static final String SEEK_FRIENDS_PAIR_QUERY = "SELECT * FROM users WHERE id IN (SELECT user1_id FROM friends WHERE user1_id = ? AND user2_id = ?)";
    private static final String INSERT_FRIENDSHIP_QUERY = "INSERT INTO friends (user1_id, user2_id) VALUES (?, ?)";
    // Константы для поиска юзера по различным данным
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
    private static final String FIND_BY_LOGIN_QUERY = "SELECT * FROM users WHERE login = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    //Creation of new user
    @Override
    public User createUser(User user) {
        insert(INSERT_USER_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return findByEmail(user.getEmail()).orElseThrow(() -> new InternalServerException("Ошибка при чтении данных пользователя"));
    }

    //Deleting of user
    @Override
    public User deleteUser(User user) {
        if (delete(DELETE_USER_QUERY, user.getId())) return user;
        else throw new InternalServerException("Не удалось удалить " + user);
    }

    @Override
    public User modifyUser(User user) {
        update(UPDATE_USER_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return findMany(FIND_ALL_USERS_QUERY);
    }

    @Override
    public List<User> getFriends(long userId) {
        return findMany(GET_FRIENDS_QUERY, userId);
    }

    @Override
    public User setNewFriendship(Long l1, Long l2) {
        insert(INSERT_FRIENDSHIP_QUERY, l1, l2);
        return findById(l2).orElseThrow(() -> new InternalServerException("Ошибка при чтении данных пользователя" + l2));
    }

    @Override
    public List<User> deleteFriendship(long l1, long l2) {
        delete(DELETE_FRIENDSHIP_QUERY, l1, l2);
        return getFriends(l1);
    }

    @Override
    public List<User> getCommonFriends(long l1, long l2) {
        return findMany(GET_COMMON_FRIENDS_QUERY, l1, l2, l1, l2);
    }

    public Optional<User> findById(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public Optional<User> findByEmail(String email) {
        return findOne(FIND_BY_EMAIL_QUERY, email);
    }

    public Optional<User> findByLogin(String login) {
        return findOne(FIND_BY_LOGIN_QUERY, login);
    }

    public boolean isFriendPairExist(long l1, long l2) {
        return findOne(SEEK_FRIENDS_PAIR_QUERY, l1, l2).isPresent();
    }

}