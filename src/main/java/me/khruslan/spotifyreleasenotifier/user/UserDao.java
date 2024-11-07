package me.khruslan.spotifyreleasenotifier.user;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao {

    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<User> getAllUsers() {
        var transaction = beginTransaction();
        var users = sessionFactory.getCurrentSession()
                .createQuery("FROM User", User.class)
                .getResultList();
        transaction.commit();
        return users;
    }

    public boolean userExists(Long telegramId) {
        var transaction = beginTransaction();
        var userExists = sessionFactory.getCurrentSession()
                .createQuery("FROM User WHERE telegramId=:telegramId", User.class)
                .setParameter("telegramId", telegramId)
                .getSingleResultOrNull() != null;
        transaction.commit();
        return userExists;
    }

    public void createUser(User user) {
        var transaction = beginTransaction();
        sessionFactory.getCurrentSession().persist(user);
        transaction.commit();
    }

    public void updateUser(User user) {
        var transaction = beginTransaction();
        sessionFactory.getCurrentSession().merge(user);
        transaction.commit();
    }

    public void deleteUser(Long telegramId) {
        var transaction = beginTransaction();
        sessionFactory.getCurrentSession()
                .createMutationQuery("DELETE FROM User WHERE telegramId=:telegramId")
                .setParameter("telegramId", telegramId)
                .executeUpdate();
        transaction.commit();
    }

    private Transaction beginTransaction() {
        return sessionFactory.getCurrentSession().beginTransaction();
    }
}
