package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //GET
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findAll() {
        return userService.values();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.debug("id is:{}", id);
        return userService.get(id);
    }

    @GetMapping("{id}/friends")
    public Collection<User> getUserFriends(@PathVariable Long id) {

        if (!userService.containsKey(id)) {
            throw new NotFoundException("Пользователь " + id + "не найден, у несуществующих пользователей, нет друзей");
        }

        return userService.getUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        if (!userService.containsKey(id)) {
            throw new NotFoundException("Пользователь " + id + "не найден, у несуществующих пользователей, нет друзей");
        }
        if (!userService.containsKey(otherId)) {
            throw new NotFoundException("Пользователь " + otherId + "не найден, нельзя добавить другом несуществующего пользователя");
        }

        return userService.getCommonFriends(id, otherId);
    }

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User newUser) {
        log.debug("POST create: {}", newUser);

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(newUser.getBirthday(), formatter).isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем: " + newUser.getBirthday());
        }

        for (User user : userService.values()) {
            if (user.getLogin().equals(newUser.getLogin())) {
                throw new DuplicatedDataException("Этот логин уже используется");
            }
            if (user.getEmail().equals(newUser.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }

        newUser.setId(getNextId());
        newUser.setFriends(new HashSet<>());

        // сохраняем нового пользователя в памяти приложения
        userService.put(newUser.getId(), newUser);
        return newUser;
    }

    private long getNextId() {
        long currentMaxId = userService.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }

    // PUT
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody User newUser) {

        log.debug("PUT update: {}", newUser.toString());
        // проверяем необходимые условия
        if (newUser.getId() == null) {
            throw new ValidationException("Необходимо указать id");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(newUser.getBirthday(), formatter).isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (userService.containsKey(newUser.getId())) {

            User oldUser = userService.get(newUser.getId());

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());

            if (newUser.getName().isBlank()) {
                oldUser.setName(newUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }

            if (newUser.getFriends() != null) {
                oldUser.setFriends(newUser.getFriends());
            }

            return oldUser;
        }

        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден.");
    }

    @PutMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addUserFriend(@PathVariable Long id, @PathVariable Long friendId) {

        if (id == null || friendId == null) {
            throw new NullPointerException("Не указан один из параметров");
        }

        if (Objects.equals(id, friendId)) {
            throw new DuplicatedDataException("Нельзя добавить другом самого пользователя");
        }
        if (!userService.containsKey(id)) {
            throw new NotFoundException("Пользователь id=" + id + " не найден, у несуществующих пользователей, нет друзей");
        }
        if (!userService.containsKey(friendId)) {
            throw new NotFoundException("Пользователь id=" + friendId + " не найден, нельзя добавить другом несуществующего пользователя");
        }

        return userService.addUserFriend(id, friendId);
    }

    // DELETE
    @DeleteMapping("{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserFriend(@PathVariable Long id, @PathVariable Long friendId) {

        if (id == null || friendId == null) {
            throw new NullPointerException("Не указан один из параметров");
        }

        if (!userService.containsKey(id)) {
            throw new NotFoundException("Пользователь " + id + "не найден, у несуществующих пользователей, нет друзей");
        }
        if (!userService.containsKey(friendId)) {
            throw new NotFoundException("Пользователь id=" + friendId + " не найден, нельзя удалить из друзей");
        }

        userService.deleteUserFriend(id, friendId);
    }

}
