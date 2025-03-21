package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@JdbcTest
@AutoConfigureTestDatabase
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbFilmoRateApplicationTests {
    private final UserDbStorage userStorage;

    private final User standartUser1 = new User("first@error.com", "firstLogin", "NameTest1",
            LocalDate.of(2000, 10, 10));
    private final User standartUser2 = new User("second@error.com", "secondLogin", "NameTest2",
            LocalDate.of(2010, 5, 5));

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.findById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindUserByEmail() {

        Optional<User> userOptional = userStorage.findByEmail("d@fdd.net");

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(user).hasFieldOrPropertyWithValue("email", "d@fdd.net");
                        }
                );
    }


    @Test
    public void testFindUserByLogin() {

        Optional<User> userOptional = userStorage.findByLogin("epsilon");

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 5L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "epsilon");
                        }
                );
    }

    @Test
    public void testCreateUser() {

        User user = standartUser1;
        User createdUser = userStorage.createUser(user);

        assertThat(createdUser).hasFieldOrPropertyWithValue("email", user.getEmail());
        assertThat(createdUser).hasFieldOrPropertyWithValue("login", user.getLogin());
        assertThat(createdUser).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(createdUser).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
    }

    @Test
    public void testModifyUser() {
        User testUser = standartUser2;
        testUser.setId(1L);

        userStorage.modifyUser(testUser);
        User user = userStorage.findById(1L).orElseThrow(() -> new InternalServerException("user not found"));
        assertThat(user).hasFieldOrPropertyWithValue("email", testUser.getEmail());
        assertThat(user).hasFieldOrPropertyWithValue("login", testUser.getLogin());
        assertThat(user).hasFieldOrPropertyWithValue("name", testUser.getName());
        assertThat(user).hasFieldOrPropertyWithValue("birthday", testUser.getBirthday());
    }

    @Test
    public void testDeleteUser() {
        User user = userStorage.findById(8L).orElseThrow(() -> new InternalServerException("user not found"));
        userStorage.deleteUser(user);
        User testUser = userStorage.findById(8L).orElse(null);
        assertThat(testUser == null).isEqualTo(true);
    }

    @Test
    public void testGetAllUsers() {
        User user = userStorage.findById(3L).orElseThrow(() -> new InternalServerException("user not found"));
        User user2 = userStorage.findById(5L).orElseThrow(() -> new InternalServerException("user not found"));

        List<User> users;
        users = userStorage.getAllUsers();
        assertThat(users.isEmpty()).isEqualTo(false);
        assertThat(users).contains(user);
        assertThat(users).contains(user2);
    }

    @Test
    public void testGetNotEmptyListFriends() {
        List<User> users;
        users = userStorage.getFriends(5L);
        assertThat(users.isEmpty()).isEqualTo(false);
        assertThat(users).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    public void testGetEmptyListFriends() {
        List<User> users;
        users = userStorage.getFriends(7L);
        assertThat(users.isEmpty()).isEqualTo(true);
    }

    @Test
    public void testSetNewFriendship() {
        User user = userStorage.findById(4L)
                .orElseThrow(() -> new InternalServerException("Not found user id=" + 4));
        List<User> users;
        userStorage.setNewFriendship(3L, 4L);
        users = userStorage.getFriends(3L);
        assertThat(users.isEmpty()).isEqualTo(false);
        assertThat(users).contains(user);
    }

    @Test
    public void testDeleteFriendship() {
        User user = userStorage.findById(4L)
                .orElseThrow(() -> new InternalServerException("Not found user id=" + 4));
        List<User> users;
        userStorage.deleteFriendship(3L, 4L);
        users = userStorage.getFriends(3L);
        assertThat(users.isEmpty()).isEqualTo(true);
    }

    @Test
    public void testGetNotEmptyListCommonFriends() {
        User user = userStorage.findById(3L)
                .orElseThrow(() -> new InternalServerException("Not found user id=" + 3));

        List<User> users;
        users = userStorage.getCommonFriends(2L, 4L);
        assertThat(users.isEmpty()).isEqualTo(false);
        assertThat(users).contains(user);
    }

    @Test
    public void testGetEmptyListCommonFriends() {
        List<User> users;
        users = userStorage.getCommonFriends(2L, 7L);
        assertThat(users.isEmpty()).isEqualTo(true);
    }

    @Test
    public void testIsFriendPairExist() {
        boolean isExists = userStorage.isFriendPairExist(5L, 6L);
        assertThat(isExists).isEqualTo(false);
        isExists = userStorage.isFriendPairExist(6L, 2L);
        assertThat(isExists).isEqualTo(true);
    }

}