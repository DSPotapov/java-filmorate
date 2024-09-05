package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
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
        log.debug("Add user : {}  {}", id, user);
        userStorage.put(id, user);
        log.debug(userStorage.values().toString());
    }

    public Set<Long> keySet() {
        return userStorage.keySet();
    }

    public boolean containsKey(Long id) {
        return userStorage.containsKey(id);
    }

    public User addUserFriend(Long id, Long friendId) {
        User user = userStorage.get(id);
        user.getFriends().add(friendId);
        User friend = userStorage.get(friendId);
        friend.getFriends().add(id);
        return user;
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
        Set<Long> userFriendsId;
        if (user.getFriends() == null) {
            userFriendsId = new HashSet<>();
        } else {
            userFriendsId = user.getFriends();
        }

        for (Long friendId : userFriendsId) {
            userFriends.add(userStorage.get(friendId));
        }
        return userFriends;
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        Set<User> commonFriends = new HashSet<>();
        User user = userStorage.get(id);
        Set<Long> userFriends = user.getFriends();
        Set<Long> otherUserFriends = userStorage.get(otherId).getFriends();
        for (Long commonId : userFriends) {
            if (otherUserFriends.contains(commonId)) {
                commonFriends.add(userStorage.get(commonId));
            }
        }
        return commonFriends;
    }

}
