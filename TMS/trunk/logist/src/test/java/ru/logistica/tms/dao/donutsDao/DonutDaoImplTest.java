package ru.logistica.tms.dao.donutsDao;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.suppliersDao.Supplier;
import ru.logistica.tms.dao.suppliersDao.SupplierDao;
import ru.logistica.tms.dao.suppliersDao.SupplierDaoImpl;

import java.sql.Date;
import java.util.Collection;

public class DonutDaoImplTest {

    private static Donut donut1;
    private static Donut donut2;
    private static DonutDao donutDao;

    @BeforeClass
    public static void setUp() throws Exception {
        TestUtil.cleanDatabase(false);
        ConnectionManager.setConnection(TestUtil.createConnection());

        // create supplier
        Supplier supplier = new Supplier();
        supplier.setInn("123456");
        SupplierDao supplierDao = new SupplierDaoImpl();
        supplierDao.save(supplier);
        ConnectionManager.getConnection().commit();

        donutDao = new DonutDaoImpl();

        donut1 = new Donut();
        donut1.setComment("comment1");
        donut1.setCreationDate(Date.valueOf("2015-04-12"));
        donut1.setDriver("driver1");
        donut1.setDriverPhoneNumber("89055487654");
        donut1.setLicensePlate("licPl1");
        donut1.setPalletsQty(4);
        donut1.setSupplier(supplier);

        donut2 = new Donut();
        donut2.setComment("comment2");
        donut2.setCreationDate(Date.valueOf("2015-05-05"));
        donut2.setDriver("driver2");
        donut2.setDriverPhoneNumber("8905522222");
        donut2.setLicensePlate("licPl2");
        donut2.setPalletsQty(1);
        donut2.setSupplier(supplier);

    }

    @AfterClass
    public static void tearDown() throws Exception {
        ConnectionManager.getConnection().close();
    }

    @Test
    public void testSave() throws Exception {
        donutDao.save(donut1);
        donutDao.save(donut2);
        ConnectionManager.getConnection().commit();
        Assert.assertTrue(donut1.getDonutId() == 1);
        Assert.assertTrue(donut2.getDonutId() == 2);
    }

    @Test(dependsOnMethods = {"testSave"})
    public void testGetById() throws Exception {
        Donut donut = donutDao.getById(1);
        ConnectionManager.getConnection().commit();
        Assert.assertEquals(DonutDaoImplTest.donut1, donut);
    }

    @Test(dependsOnMethods = {"testGetById"})
    public void testUpdate() throws Exception {
        donut1.setComment("updatedComment");
        donut1.setCreationDate(Date.valueOf("2015-04-11"));
        donutDao.update(donut1);
        Donut updatedDonut = donutDao.getById(1);
        ConnectionManager.getConnection().commit();
        Assert.assertEquals(donut1, updatedDonut);
    }

    @Test(dependsOnMethods = {"testUpdate"})
    public void testGetAll() throws Exception {
        Collection<Donut> all = donutDao.getAll();
        ConnectionManager.getConnection().commit();
        Assert.assertTrue(all.size() == 2);
    }



}