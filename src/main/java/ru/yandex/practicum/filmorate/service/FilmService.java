package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Set;


@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> values() {
        return filmStorage.values();
    }

    public Film get(Long id) {
        return filmStorage.get(id);
    }

    public void put(Long id, Film film) {
        filmStorage.put(id, film);
    }

    public Set<Long> keySet() {
        return filmStorage.keySet();
    }

    public boolean containsKey(Long id) {
        return filmStorage.containsKey(id);
    }
}
