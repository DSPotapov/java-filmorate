package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmTest {
    @Test
    public void testFilm() {




        Film film = Film.builder()
                .name("Фильм")
                .description("Описание фильма.")
                .releaseDate(LocalDate.of(2023, 1, 1).toString())
                .duration(120)
                .build();

//        assertTrue(film.isValid());
    }
}