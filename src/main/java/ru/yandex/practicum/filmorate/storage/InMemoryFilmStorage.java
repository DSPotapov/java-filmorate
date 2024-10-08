package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> values() {
        log.debug("UserStorage.values()");
        return films.values();
    }

    @Override
    public Film get(Long id) {
        return films.get(id);
    }

    @Override
    public void put(Long id, Film film) {
        films.put(id, film);
    }

    @Override
    public Set<Long> keySet() {
        return films.keySet();
    }

    @Override
    public boolean containsKey(Long id) {
        return films.containsKey(id);
    }
}
