package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
