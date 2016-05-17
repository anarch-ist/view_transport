package ru.logistica.tms.dao2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * if you use sessionFactory.getCurrentSession(), you'll obtain a "current session" which is bound to the lifecycle
 * of the transaction and will be automatically flushed and closed when the transaction ends (commit or rollback).
 *
 * getCurrentSession returns new session or already existed. ThreadLocalSessionContext has strategy: session for thread.
 * we can use this context by mean of <property name="current_session_context_class">thread</property>
 */
public class HibernateUtils {
    private static Logger logger = LogManager.getLogger();
    private static SessionFactory sessionFactory;

    public static void setSessionFactory(SessionFactory sessionFactory) {
        HibernateUtils.sessionFactory = sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void beginTransaction() {
        sessionFactory.getCurrentSession().beginTransaction();
    }

    public static void commitTransaction() {
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    public static void rollbackTransaction() {
        logger.error("START ROLLBACK");
        sessionFactory.getCurrentSession().getTransaction().rollback();
        logger.error("END ROLLBACK");
    }

    public static Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
