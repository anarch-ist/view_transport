package ru.logistica.tms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.logistica.tms.dao2.HibernateUtils;
import ru.logistica.tms.dao2.userDao.User;

import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.util.TimeZone;

@WebListener
public class InitializeListener implements ServletContextListener {
    public static final Logger logger = LogManager.getLogger();
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        try {
//            InitialContext cxt = new InitialContext();
//            DataSource ds = (DataSource) cxt.lookup( "java:/comp/env/jdbc/postgres" );

//            Configuration configuration = new Configuration();
//            configuration.configure("hibernate.cfg.xml");
//            logger.info("Hibernate Configuration created successfully");
//
//            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//            logger.info("ServiceRegistry created successfully");
//            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//            logger.info("SessionFactory created successfully");
//
//            sce.getServletContext().setAttribute("SessionFactory", sessionFactory);
//            logger.info("Hibernate SessionFactory Configured successfully");
//            HibernateUtils.setSessionFactory(sessionFactory);

            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure( "hibernate.cfg.xml" )
                    .build();

            Metadata metadata = new MetadataSources( standardRegistry )
                    //.addAnnotatedClass(User.class)
                    // You can add more entity classes here like above
                    //.addResource( "Mapping.hbm.xml" )
                    .getMetadataBuilder()
                    .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE )
                    .build();

            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
            HibernateUtils.setSessionFactory(sessionFactory);
//            Configuration configuration = new Configuration();
//            configuration.configure();
//            //System.out.println(configuration.getProperties());
//            HibernateUtils.setSessionFactory(configuration.buildSessionFactory());
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //HibernateUtils.getSessionFactory().close();
        SessionFactory sessionFactory = (SessionFactory) sce.getServletContext().getAttribute("SessionFactory");
        if(sessionFactory != null && !sessionFactory.isClosed()){
            logger.info("Closing sessionFactory");
            sessionFactory.close();
        }
        logger.info("Released Hibernate sessionFactory resource");
    }
}
