package ru.logistica.tms;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import ru.logistica.tms.dao2.HibernateUtils;
import ru.logistica.tms.dao2.docDao.Doc;
import ru.logistica.tms.dao2.docPeriodDao.DocPeriod;
import ru.logistica.tms.dao2.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao2.orderDao.Order;
import ru.logistica.tms.dao2.supplierDao.Supplier;
import ru.logistica.tms.dao2.userDao.*;
import ru.logistica.tms.dao2.warehouseDao.Warehouse;

import java.util.TimeZone;

public abstract class HibernateTest {

    @BeforeClass
    public static void setUp() throws Exception {
        TestUtil.cleanDatabase(false);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // setup the session factory
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Supplier.class);
        configuration.addAnnotatedClass(DocPeriod.class);
        configuration.addAnnotatedClass(DonutDocPeriod.class);
        configuration.addAnnotatedClass(Doc.class);
        configuration.addAnnotatedClass(Warehouse.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Permission.class);
        configuration.addAnnotatedClass(UserRole.class);
        configuration.addAnnotatedClass(SupplierUser.class);
        configuration.addAnnotatedClass(WarehouseUser.class);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL94Dialect");
        configuration.setProperty("hibernate.connection.driver_class","org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres");
        configuration.setProperty("hibernate.connection.autocommit", "false");
        configuration.setProperty("hibernate.connection.username", "postgres");
        configuration.setProperty("hibernate.connection.password", "postgres");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("hibernate.connection_pool_size", "1");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.format_sql", "true");
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        HibernateUtils.setSessionFactory(sessionFactory);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        HibernateUtils.getSessionFactory().close();
    }

}