package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class User {
    int id;
    String email;
    String login;
    String name;
    String birthday;
}
