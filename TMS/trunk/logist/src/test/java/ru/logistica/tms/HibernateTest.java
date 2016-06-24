package ru.logistica.tms;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import ru.logistica.tms.dao.HibernateUtils;
import ru.logistica.tms.dao.UtcTimestampType;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.docPeriodDao.DocPeriod;
import ru.logistica.tms.dao.docPeriodDao.DonutDocPeriod;
import ru.logistica.tms.dao.orderDao.Order;
import ru.logistica.tms.dao.supplierDao.Supplier;
import ru.logistica.tms.dao.userDao.*;
import ru.logistica.tms.dao.warehouseDao.Warehouse;

import java.util.TimeZone;

public abstract class HibernateTest {

    @BeforeClass
    public void setUp() throws Exception {
        //TestUtil.recreateDatabase();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // setup the session factory
        Configuration configuration = new Configuration();
        configuration.registerTypeOverride(new UtcTimestampType());
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
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        configuration.setProperty("hibernate.connection.driver_class","org.hsqldb.jdbcDriver");
        configuration.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:postgres");
        configuration.setProperty("hibernate.connection.autocommit", "false");
        configuration.setProperty("hibernate.connection.username", "SA");
        configuration.setProperty("hibernate.connection.password", "");
        configuration.setProperty("hibernate.current_session_context_class", "thread");
        configuration.setProperty("hibernate.connection_pool_size", "1");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.show_sql", "true");
//        configuration.setProperty("hibernate.show_sql", "false");
        configuration.setProperty("hibernate.format_sql", "true");
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        HibernateUtils.setSessionFactory(sessionFactory);
    }

    @AfterClass
    public void tearDown() throws Exception {
        HibernateUtils.getSessionFactory().close();
    }

}
