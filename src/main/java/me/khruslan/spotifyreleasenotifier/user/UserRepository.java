package me.khruslan.spotifyreleasenotifier.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public boolean userExists(Long telegramId) {
        return users.stream().anyMatch(user -> user.telegramId().equals(telegramId));
    }

    public void createUser(User user) {
        users.add(user);
    }
}
