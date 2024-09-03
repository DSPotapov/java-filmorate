package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {

    public Collection<User> values();

    public User get(Long id);

    public void put(Long id, User user);

    public Set<Long> keySet();

    public boolean containsKey(Long id);

}
