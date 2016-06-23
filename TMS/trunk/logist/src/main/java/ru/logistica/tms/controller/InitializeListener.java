package ru.logistica.tms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.HibernateUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.TimeZone;

@WebListener
public class InitializeListener implements ServletContextListener {
    public static final Logger logger = LogManager.getLogger();
    private static SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Start context init");
        try {
            // set TimeZone as UTC
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

            // init db connection pool and make default selects
            Configuration configuration = new Configuration().configure();
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.configure().buildSessionFactory(serviceRegistry);
            HibernateUtils.setSessionFactory(sessionFactory);
            DaoFacade.fillOffsetsForAbbreviations();
        } catch (Exception e) {
            logger.error(e);
        }
        logger.info("context init successfully");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            logger.info("Released Hibernate sessionFactory resource");
        }
    }
}
