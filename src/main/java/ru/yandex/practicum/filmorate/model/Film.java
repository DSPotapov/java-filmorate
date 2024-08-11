package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    String releaseDate;
    @Positive
    int duration;
}
