package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

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
    private final LocalDate BIRTHDAY_OF_CINEMA = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        log.debug("POST create: {}", newFilm);
        // проверяем выполнение необходимых условий
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            throw new ValidationException("Название фильма должно быть указано");
        }
        if (newFilm.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не больше 200 символов");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(newFilm.getReleaseDate(), formatter).isBefore(BIRTHDAY_OF_CINEMA)) {
            throw new ValidationException("Фильм мог выйти только после 28 декабря 1895 года");
        }
        if (newFilm.getDuration().isNegative()) {
            log.debug("PUT update Некорректная длительность фильма: {}", newFilm.getDuration().toString());
            throw new ValidationException("Длительность фильма не может быть отрицательной");
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
    public Film update(@RequestBody Film newFilm) {

        log.debug("PUT update: {}", newFilm.toString());
        // проверяем необходимые условия
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            throw new ValidationException("Название фильма должно быть указано");
        }
        if (newFilm.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не больше 200 символов");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(newFilm.getReleaseDate(), formatter).isBefore(BIRTHDAY_OF_CINEMA)) {
            throw new ValidationException("Фильм мог выйти только после 28 декабря 1895 года");
        }
        if (!newFilm.getDuration().isPositive()) {
            log.debug("PUT update Некорректная длительность фильма: {}", newFilm.getDuration().toString());
            throw new ValidationException("Длительность фильма не может быть отрицательной");
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

            return oldFilm;
        }

        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден.");
    }
}
