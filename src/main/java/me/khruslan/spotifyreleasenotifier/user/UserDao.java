package me.khruslan.spotifyreleasenotifier.user;

import me.khruslan.spotifyreleasenotifier.core.db.Dao;
import me.khruslan.spotifyreleasenotifier.user.model.UserDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDao extends Dao {
    private static final String QUERY_SELECT_ALL_USERS = "FROM User";
    private static final String QUERY_SELECT_USER_BY_TELEGRAM_ID = "FROM User WHERE telegramId=:telegramId";
    private static final String QUERY_DELETE_USER_BY_TELEGRAM_ID = "DELETE FROM User WHERE telegramId=:telegramId";
    private static final String PARAM_TELEGRAM_ID = "telegramId";

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory, logger);
    }

    public List<UserDto> getAllUsers() {
        var result = runTransaction(this::getAllUsersTransaction);
        return result.getOrDefault(new ArrayList<>());
    }

    public boolean userExists(long telegramId) {
        var result = runTransaction(this::userExistsTransaction, telegramId);
        return result.getOrDefault(false);
    }

    public boolean createUser(UserDto user) {
        var result = runTransaction(this::createUserTransaction, user);
        return result.isSuccess();
    }

    public boolean updateUser(UserDto user) {
        var result = runTransaction(this::updateUserTransaction, user);
        return result.isSuccess();
    }

    public boolean deleteUser(long telegramId) {
        var result = runTransaction(this::deleteUserTransaction, telegramId);
        return result.getOrDefault(false);
    }

    private List<UserDto> getAllUsersTransaction(Session session) {
        logger.debug("Executing select all users transaction");
        return session.createQuery(QUERY_SELECT_ALL_USERS, UserDto.class).getResultList();
    }

    private boolean userExistsTransaction(Session session, long telegramId) {
        logger.debug("Executing select user transaction: telegramId={}", telegramId);
        return session.createQuery(QUERY_SELECT_USER_BY_TELEGRAM_ID, UserDto.class)
                .setParameter(PARAM_TELEGRAM_ID, telegramId)
                .getSingleResultOrNull() != null;
    }

    private Void createUserTransaction(Session session, UserDto user) {
        logger.debug("Executing create user transaction: user={}", user);
        session.persist(user);
        return null;
    }

    private UserDto updateUserTransaction(Session session, UserDto user) {
        logger.debug("Executing update user transaction: user={}", user);
        return session.merge(user);
    }

    private boolean deleteUserTransaction(Session session, long telegramId) {
        logger.debug("Executing delete user transaction: telegramId={}", telegramId);
        return session.createMutationQuery(QUERY_DELETE_USER_BY_TELEGRAM_ID)
                .setParameter(PARAM_TELEGRAM_ID, telegramId)
                .executeUpdate() > 0;
    }
}
