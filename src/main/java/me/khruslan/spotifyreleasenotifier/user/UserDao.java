package me.khruslan.spotifyreleasenotifier.user;

import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public boolean userExists(Long telegramId) {
        var transaction = sessionFactory.getCurrentSession().beginTransaction();
        var userExists = sessionFactory.getCurrentSession()
                .createQuery("from User where telegramId=:telegramId", User.class)
                .setParameter("telegramId", telegramId)
                .getSingleResultOrNull() != null;
        transaction.commit();
        return userExists;
    }

    public void createUser(User user) {
        var transaction = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().persist(user);
        transaction.commit();
    }
}
