package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final LocalDate BIRTHDAYOFCINEMA = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film newFilm) {
        log.debug("POST create: {}", newFilm);
        // проверяем выполнение необходимых условий
        if (newFilm.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не больше 200 символов");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(newFilm.getReleaseDate(), formatter).isBefore(BIRTHDAYOFCINEMA)) {
            throw new ValidationException("Фильм мог выйти только после 28 декабря 1895 года");
        }

        newFilm.setId(getNextId());

        // сохраняем нового  пользователя в памяти приложения
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {

        log.debug("PUT update: {}", newFilm.toString());

        // проверяем необходимые условия
        if (newFilm.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не больше 200 символов");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(newFilm.getReleaseDate(), formatter).isBefore(BIRTHDAYOFCINEMA)) {
            throw new ValidationException("Фильм мог выйти только после 28 декабря 1895 года");
        }

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            // если публикация найдена и все условия соблюдены, обновляем её содержимое
            if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null && !newFilm.getDescription().isBlank()) {
                oldFilm.setDescription(newFilm.getDescription());
            }

            oldFilm.setReleaseDate(LocalDate.parse(newFilm.getReleaseDate(), formatter).toString());
            oldFilm.setDuration(newFilm.getDuration());

            return oldFilm;
        }

        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден.");
    }
}
