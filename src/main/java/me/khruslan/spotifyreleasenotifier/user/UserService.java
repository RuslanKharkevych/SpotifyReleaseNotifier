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
        var usersDto = userDao.getAllUsers();
        var users = userMapper.mapFromDto(usersDto);
        logger.debug("Fetched users: {}", users);
        return users;
    }

    public boolean userExists(long telegramId) {
        if (userDao.userExists(telegramId)) {
            logger.debug("Found user with telegramId: {}", telegramId);
            return true;
        } else {
            logger.debug("Couldn't find user with telegramId: {}", telegramId);
            return false;
        }
    }

    public void createUser(User user) {
        var userDto = userMapper.mapToDto(user);
        userDao.createUser(userDto);
        logger.debug("Created user: {}", user);
    }

    public void updateUser(User user) {
        var userDto = userMapper.mapToDto(user);
        userDao.updateUser(userDto);
        logger.debug("Updated user: {}", user);
    }

    public void deleteUser(long telegramId) {
        userDao.deleteUser(telegramId);
        logger.debug("Deleted user with telegramId: {}", telegramId);
    }
}
