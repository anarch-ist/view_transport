package ru.logistica.tms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.logistica.tms.dao.DaoFacade;
import ru.logistica.tms.dao.HibernateUtils;
import ru.logistica.tms.dao.userDao.User;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.TimeZone;

@WebListener
public class InitializeListener implements ServletContextListener {
    public static final Logger logger = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        try {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();

            Metadata metadata = new MetadataSources(standardRegistry)
                    .getMetadataBuilder()
                    .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                    .build();

            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
            HibernateUtils.setSessionFactory(sessionFactory);

            DaoFacade.fillOffsetsForAbbreviations();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //HibernateUtils.getSessionFactory().close();
        SessionFactory sessionFactory = (SessionFactory) sce.getServletContext().getAttribute("SessionFactory");
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            logger.info("Closing sessionFactory");
            sessionFactory.close();
        }
        logger.info("Released Hibernate sessionFactory resource");
    }
}
