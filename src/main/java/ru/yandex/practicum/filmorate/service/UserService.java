package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> makeNewFriendsPair(Long l1, Long l2) {
        userStorage.findById(l1).orElseThrow(() -> new NotFoundException("User id = " + l1 + " not exist", l1));
        userStorage.findById(l2).orElseThrow(() -> new NotFoundException("User id = " + l2 + " not found", l2));
        if (userStorage.isFriendPairExist(l1, l2)) {
            log.warn("\nFriends pair {} and {} already exists", l1, l2);
            throw new DuplicateDataException("Friends pair " + l1 + " and " + l2 + " already exists", "");
        }
        userStorage.setNewFriendship(l1, l2);
        return userStorage.getFriends(l1).stream().map(UserMapper::mapToUserDto).toList();
    }

    public List<UserDto> getCommonFriends(Long l1, Long l2) {
        userStorage.findById(l1).orElseThrow(() -> new NotFoundException("User id = " + l1 + " not exist", l1));
        userStorage.findById(l2).orElseThrow(() -> new NotFoundException("User id = " + l2 + " not found", l2));
        return userStorage.getCommonFriends(l1, l2).stream().map(UserMapper::mapToUserDto).toList();
    }

    public List<UserDto> getFriends(Long l) {
        userStorage.findById(l).orElseThrow(() -> new NotFoundException("User id = " + l + " not exist", l));
        return userStorage.getFriends(l).stream().map(UserMapper::mapToUserDto).toList();
    }

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream().map(UserMapper::mapToUserDto).toList();
    }

    public UserDto createUser(UserRequest request) {
        if (userStorage.findByEmail(request.getEmail()).isPresent()) {
            log.warn("\nNot created {}", request);
            throw new DuplicateDataException("Email " + request.getEmail() + " is already used.", request);
        }
        if (userStorage.findByLogin(request.getLogin()).isPresent()) {
            log.warn("\nNot created {}", request);
            throw new DuplicateDataException("Login " + request.getLogin() + " is already used.", request);
        }
        User user = UserMapper.mapToUser(request);
        if (!request.hasName()) user.setName(user.getLogin());
        return UserMapper.mapToUserDto(userStorage.createUser(user));
    }

    public UserDto changeUsersData(UserRequest request) {
        //Проверяем данные
        Long id = request.getId();
        User testUser = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь id = " + id + " не найден ", request));
        testUser = userStorage.findByEmail(request.getEmail()).orElse(null);
        if ((testUser != null) && (testUser.getId() != id)) {
            throw new DuplicateDataException("Email " + request.getEmail() + " is already used.", request);
        }
        testUser = userStorage.findByLogin(request.getLogin()).orElse(null);
        if ((testUser != null) && (testUser.getId() != id)) {
            throw new DuplicateDataException("Login " + request.getLogin() + " is already used.", request);
        }
        User user = UserMapper.mapToUser(request);
        user.setId(id);
        if (!request.hasName()) {
            user.setName(user.getLogin());
        }
        return UserMapper.mapToUserDto(userStorage.modifyUser(user));

    }

    public List<UserDto> deleteFromFriends(Long l1, Long l2) {
        userStorage.findById(l1).orElseThrow(() -> new NotFoundException("User id = " + l1 + " not exist", l1));
        userStorage.findById(l2).orElseThrow(() -> new NotFoundException("User id = " + l2 + " not found", l2));
        return userStorage.deleteFriendship(l1, l2).stream().map(UserMapper::mapToUserDto).toList();
    }

    public UserDto deleteUser(long id) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь id = " + id + " не найден ", id));
        return UserMapper.mapToUserDto(userStorage.deleteUser(user));
    }

}