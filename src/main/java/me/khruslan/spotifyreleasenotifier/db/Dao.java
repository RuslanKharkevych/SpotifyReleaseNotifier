package me.khruslan.spotifyreleasenotifier.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Dao {
    private final SessionFactory sessionFactory;
    private final Logger logger;

    protected Dao(SessionFactory sessionFactory, Logger logger) {
        this.sessionFactory = sessionFactory;
        this.logger = logger;
    }

    protected <T> TransactionResult<T> runTransaction(Function<Session, T> statement) {
        Transaction transaction = null;

        try {
            var session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();
            var result = statement.apply(session);
            transaction.commit();
            logger.debug("Transaction succeeded: result={}", result);
            return new TransactionResult.Success<>(result);
        } catch (RuntimeException e) {
            logger.error("Transaction failed", e);
            if (transaction != null) transaction.rollback();
            return new TransactionResult.Error<>();
        }
    }

    protected <T, U> TransactionResult<T> runTransaction(BiFunction<Session, U, T> statement, U parameter) {
        return runTransaction(session -> statement.apply(session, parameter));
    }
}
