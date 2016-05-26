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
    private static StandardServiceRegistry serviceRegistry;
    private static SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        try {
            Configuration configuration = new Configuration().configure();
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.configure().buildSessionFactory(serviceRegistry);
//            Configuration configuration = new Configuration();
//            configuration.configure();
//            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().applySetting(configuration.getProperties()).build();
//
//            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
//            sessionFactory = configuration.buildSessionFactory(serviceRegistry);


// hib 5.0.0.final init
//            Metadata metadata = new MetadataSources(standardRegistry)
//                    .getMetadataBuilder()
//                    .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
//                    .build();
//
//            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
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
