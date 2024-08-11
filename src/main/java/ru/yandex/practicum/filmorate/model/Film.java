package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;

/**
 * Film.
 */
@Data
public class Film {
    Long id;
    @NotBlank()
    String name;
    @NotBlank
    String description;
    String releaseDate;
    @Positive
    int duration;
}
