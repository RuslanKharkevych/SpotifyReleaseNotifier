package me.khruslan.spotifyreleasenotifier.user;

import me.khruslan.spotifyreleasenotifier.user.model.UserDto;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

// TODO: Logging & error handling
@Component
public class UserDao {
    private static final String TABLE_USERS = "User";
    private static final String COLUMN_TELEGRAM_ID = "telegramId";
    private static final String PARAM_TELEGRAM_ID = COLUMN_TELEGRAM_ID;

    private static final String QUERY_SELECT_ALL_USERS = "FROM " + TABLE_USERS;
    private static final String QUERY_SELECT_USER_BY_TELEGRAM_ID =
            QUERY_SELECT_ALL_USERS + " WHERE " + COLUMN_TELEGRAM_ID + "=:" + PARAM_TELEGRAM_ID;
    private static final String QUERY_DELETE_USER_BY_TELEGRAM_ID = "DELETE " + QUERY_SELECT_USER_BY_TELEGRAM_ID;

    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<UserDto> getAllUsers() {
        var transaction = beginTransaction();
        var users = sessionFactory.getCurrentSession()
                .createQuery(QUERY_SELECT_ALL_USERS, UserDto.class)
                .getResultList();
        transaction.commit();
        return users;
    }

    public boolean userExists(long telegramId) {
        var transaction = beginTransaction();
        var userExists = sessionFactory.getCurrentSession()
                .createQuery(QUERY_SELECT_USER_BY_TELEGRAM_ID, UserDto.class)
                .setParameter(PARAM_TELEGRAM_ID, telegramId)
                .getSingleResultOrNull() != null;
        transaction.commit();
        return userExists;
    }

    public void createUser(UserDto user) {
        var transaction = beginTransaction();
        sessionFactory.getCurrentSession().persist(user);
        transaction.commit();
    }

    public void updateUser(UserDto user) {
        var transaction = beginTransaction();
        sessionFactory.getCurrentSession().merge(user);
        transaction.commit();
    }

    public void deleteUser(long telegramId) {
        var transaction = beginTransaction();
        sessionFactory.getCurrentSession()
                .createMutationQuery(QUERY_DELETE_USER_BY_TELEGRAM_ID)
                .setParameter(PARAM_TELEGRAM_ID, telegramId)
                .executeUpdate();
        transaction.commit();
    }

    private Transaction beginTransaction() {
        return sessionFactory.getCurrentSession().beginTransaction();
    }
}
