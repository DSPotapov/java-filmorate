package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        // проверяем выполнение необходимых условий
        if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
            throw new ValidationException("Имейл должен быть указан");
        }
        if (!newUser.getEmail().contains("@")) {
            throw new ValidationException("Некорректный имейл -> должен содержать @");
        }
        if (newUser.getLogin() == null || newUser.getLogin().isBlank()) {
            throw new ValidationException("Логин должен быть указан");
        }
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

            for (User user : users.values()) {
                if (user.getEmail().equals(newUser.getEmail())) {
                    throw new DuplicatedDataException("Этот имейл уже используется");
                }
            }

        // формируем дополнительные данные
        newUser.setId(getNextId());
        newUser.setRegistrationDate(Instant.now());
        // сохраняем нового  пользователя в памяти приложения
        users.put(newUser.getId(), newUser);
        return newUser;
    }


}
