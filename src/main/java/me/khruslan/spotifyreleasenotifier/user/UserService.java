package me.khruslan.spotifyreleasenotifier.user;

import me.khruslan.spotifyreleasenotifier.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserDao userDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    public List<User> getAllUsers() {
        logger.debug("Fetching users");
        var usersDto = userDao.getAllUsers();
        var users = userMapper.mapFromDto(usersDto);
        logger.debug("Fetched users: {}", users);
        return users;
    }

    public boolean userExists(long telegramId) {
        logger.debug("Checking if user exists: telegramId={}", telegramId);

        if (userDao.userExists(telegramId)) {
            logger.debug("User found");
            return true;
        } else {
            logger.debug("User not found");
            return false;
        }
    }

    public boolean createUser(User user) {
        logger.debug("Creating user: {}", user);
        var userDto = userMapper.mapToDto(user);

        if (userDao.createUser(userDto)) {
            logger.debug("Successfully created user");
            return true;
        } else {
            logger.debug("Failed to create user");
            return false;
        }
    }

    public void updateUser(User user) {
        logger.debug("Updating user: {}", user);
        var userDto = userMapper.mapToDto(user);

        if (userDao.updateUser(userDto)) {
            logger.debug("Successfully updated user");
        } else {
            logger.debug("Failed to update user");
        }
    }

    public boolean deleteUser(long telegramId) {
        logger.debug("Deleting user: telegramId={}", telegramId);

        if (userDao.deleteUser(telegramId)) {
            logger.debug("Successfully deleted user");
            return true;
        } else {
            logger.debug("Failed to delete user");
            return false;
        }
    }
}
