package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Instant;

@Data
public class User {
    Long id;
    String email;
    final String login;
    String name;
    final String birthday;
}
