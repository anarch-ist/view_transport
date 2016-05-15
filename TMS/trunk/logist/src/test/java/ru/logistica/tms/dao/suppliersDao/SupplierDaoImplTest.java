package ru.logistica.tms.dao.suppliersDao;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.TestUtil;

public class SupplierDaoImplTest {

    private static SupplierDao supplierDao;
    private static Supplier supplier;

    @BeforeClass
    public static void setUp() throws Exception {
        TestUtil.cleanDatabase(false);
        ConnectionManager.setConnection(TestUtil.createConnection());
        supplier = new Supplier();
        supplierDao = new SupplierDaoImpl();
    }

    @AfterClass
    public void tearDown() throws Exception {
        ConnectionManager.getConnection().close();
    }

    @Test
    public void testSave() throws Exception {
        supplier.setInn("12323435");
        supplierDao.save(supplier);
        ConnectionManager.getConnection().commit();
        Assert.assertTrue(supplier.getSupplierID() == 1);
    }

    @Test(dependsOnMethods = {"testSave"})
    public void testGetById() throws Exception {
        Supplier supplier = supplierDao.getById(1);
        ConnectionManager.getConnection().commit();
        Assert.assertEquals(SupplierDaoImplTest.supplier, supplier);
    }


}