package me.khruslan.spotifyreleasenotifier.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public boolean userExists(Long telegramId) {
        if (repository.userExists(telegramId)) {
            logger.debug("Found user with telegramId: {}", telegramId);
            return true;
        } else {
            logger.debug("Couldn't find user with telegramId: {}", telegramId);
            return false;
        }
    }

    public void createUser(User user) {
        repository.createUser(user);
        logger.debug("Created user: {}", user);
    }
}
