package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage{

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> values() {
        return users.values();
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public void put(Long id, User user) {
        users.put(id, user);
    }

    @Override
    public Set<Long> keySet() {
        return users.keySet();
    }

    @Override
    public boolean containsKey(Long id) {
        return users.containsKey(id);
    }
}
