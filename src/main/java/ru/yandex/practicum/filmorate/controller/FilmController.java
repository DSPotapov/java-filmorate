package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

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
    private final LocalDate birthdayOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.values();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        log.debug("id is:{}", id);
        return filmService.get(id);
    }

    @GetMapping("popular")
    public Collection<Film> findAll(@RequestParam(defaultValue = "10") int count) {
        if (count < 1) {
            throw new ValidationException("Число для поиcка фильмов должен быть больше нуля");
        }

        return filmService.findAll(count);
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
        if (LocalDate.parse(newFilm.getReleaseDate(), formatter).isBefore(birthdayOfCinema)) {
            throw new ValidationException("Фильм мог выйти только после 28 декабря 1895 года");
        }

        newFilm.setId(getNextId());

        // сохраняем нового пользователя в памяти приложения
        filmService.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    private long getNextId() {
        long currentMaxId = filmService.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film update(@Valid @RequestBody Film newFilm) {

        log.debug("PUT update: {}", newFilm.toString());

        // проверяем необходимые условия
        if (newFilm.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть не больше 200 символов");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(newFilm.getReleaseDate(), formatter).isBefore(birthdayOfCinema)) {
            throw new ValidationException("Фильм мог выйти только после 28 декабря 1895 года");
        }

        if (filmService.containsKey(newFilm.getId())) {
            Film oldFilm = filmService.get(newFilm.getId());

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

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handle(final IncorrectCountException e) {
//        return new ErrorResponse(
//                "Ошибка с параметром count.", e.getMessage()
//        );
//    }
}
