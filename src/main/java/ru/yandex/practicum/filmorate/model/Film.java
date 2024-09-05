package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@Getter
@Setter
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    String releaseDate;
    @Positive
    private int duration;
    private Set<Long> userLikes = new HashSet<>();
    private int rating;

}
