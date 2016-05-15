package ru.logistica.tms.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class InitializeListener implements ServletContextListener {
    public static final Logger logger = LogManager.getLogger();
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/postgres");
            if (dataSource == null)
                logger.error("JNDI dataSource cannot be initialized");

            //DaoManager.setDataSource(dataSource);
        } catch (NamingException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
