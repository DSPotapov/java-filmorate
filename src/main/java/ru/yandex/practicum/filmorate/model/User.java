package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class User {
    Long id;
    @Email
    @NotBlank
    String email;
    @NotBlank
    String login;
    String name;
    String birthday;
}
