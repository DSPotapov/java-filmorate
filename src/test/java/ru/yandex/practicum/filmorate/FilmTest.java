package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmTest {
    @Test
    public void testFilm() {
        Film film = new Film();
        film.setName("Фильм");
        film.setDescription("Описание фильма.");
        film.setReleaseDate(LocalDate.of(2023, 1, 1).toString());
        film.setDuration(120);

//        assertTrue(film.isValid());
    }
}