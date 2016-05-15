package ru.logistica.tms.dao2.suppliersDao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao2.HibernateUtils;

public class SuppliersDaoImplTest {

    @BeforeClass
    public static void setUp() throws Exception {
        TestUtil.cleanDatabase(false);
        // setup the session factory
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(SuppliersEntity.class);
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

    @Test
    public void testSave() throws Exception {
        HibernateUtils.beginTransaction();
        SuppliersEntity suppliersEntity = new SuppliersEntity();
        suppliersEntity.setInn("1223445");
        SuppliersDao suppliersDao = new SuppliersDaoImpl();
        Integer generatedKey = suppliersDao.save(suppliersEntity);
        HibernateUtils.commitTransaction();
        Assert.assertTrue(generatedKey == 1);
    }


    @Test(dependsOnMethods = {"testSave"})
    public void testFindById() throws Exception {
        HibernateUtils.beginTransaction();
        SuppliersDao suppliersDao = new SuppliersDaoImpl();
        SuppliersEntity supplier = suppliersDao.findById(SuppliersEntity.class, 1);
        HibernateUtils.commitTransaction();
        Assert.assertEquals(supplier.getInn(), "1223445");
        Assert.assertEquals(supplier.getSupplierid().intValue(), 1);
    }

    @Test(dependsOnMethods = {"testFindById"})
    public void testUpdate() throws Exception {
        HibernateUtils.beginTransaction();
        SuppliersDao suppliersDao = new SuppliersDaoImpl();
        SuppliersEntity suppliersEntity = new SuppliersEntity();
        suppliersEntity.setInn("11111111");
        suppliersEntity.setSupplierid(1);
        suppliersDao.update(suppliersEntity);
        SuppliersEntity updatedSupplier = suppliersDao.findById(SuppliersEntity.class, 1);
        HibernateUtils.commitTransaction();
        Assert.assertEquals(updatedSupplier, suppliersEntity);
    }




}