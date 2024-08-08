package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;

/**
 * Film.
 */
@Data
public class Film {
    Long id;
    String name;
    String description;
    final String releaseDate;
    final Duration duration;
}
