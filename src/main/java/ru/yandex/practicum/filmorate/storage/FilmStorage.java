package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {

    public Collection<Film> values();

    public Film get(Long id);

    public void put(Long id, Film film);

    public Set<Long> keySet();

    public boolean containsKey(Long id);
}
