package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private FilmService filmService;

    @Autowired
    Gson gson;

    @Test
    public void createFilmIsValid() throws Exception {

        Film film = Film.builder()
                .name("Фильм")
                .description("Описание фильма.")
                .releaseDate(LocalDate.of(2023, 1, 1).toString())
                .duration(120)
                .build();
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isCreated());
    }

    @Test
    void createFilmFailedWhenNameIsEmpty() throws Exception {
        Film film = Film.builder()
                .description("Описание фильма.")
                .releaseDate(LocalDate.of(2023, 1, 1).toString())
                .duration(120)
                .build();
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserFailedWhenDescriptionIsEmpty() throws Exception {
        Film film = Film.builder()
                .name("Фильм")
                .releaseDate(LocalDate.of(2023, 1, 1).toString())
                .duration(120)
                .build();
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserFailedWhenDurationIsNegative() throws Exception {
        Film film = Film.builder()
                .name("Фильм")
                .description("Описание фильма.")
                .releaseDate(LocalDate.of(2023, 1, 1).toString())
                .duration(-120)
                .build();
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(film)))
                .andExpect(status().isBadRequest());
    }
}