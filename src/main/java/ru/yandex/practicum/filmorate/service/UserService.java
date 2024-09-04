package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> values() {
        return userStorage.values();
    }

    public User get(Long id) {
        return userStorage.get(id);
    }

    public void put(Long id, User user) {
        userStorage.put(id, user);
    }

    public Set<Long> keySet() {
        return userStorage.keySet();
    }

    public boolean containsKey(Long id) {
        return userStorage.containsKey(id);
    }

    public void addUserFriend(Long id, Long friendId) {
        User user = userStorage.get(id);
        user.getFriends().add(friendId);
        User friend = userStorage.get(friendId);
        friend.getFriends().add(id);
    }

    public void deleteUserFriend(Long id, Long friendId) {
        User user = userStorage.get(id);
        user.getFriends().remove(friendId);
        User friend = userStorage.get(friendId);
        friend.getFriends().remove(id);
    }

    public Collection<User> getUserFriends(Long id) {
        Set<User> userFriends = new HashSet<>();
        User user = userStorage.get(id);
        for (Long friendId : user.getFriends()) {
            userFriends.add(userStorage.get(friendId));
        }
        return userFriends;
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        Set<User> commonFriends = new HashSet<>();
        User user = userStorage.get(id);
        Set<Long> otherUserFriends = userStorage.get(otherId).getFriends();
        for (Long commonId : user.getFriends()) {
            if (otherUserFriends.contains(commonId)) {
                commonFriends.add(userStorage.get(commonId));
            }
        }
        return commonFriends;
    }

}
