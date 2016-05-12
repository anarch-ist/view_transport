package ru.logistica.tms.dao.suppliersDao;

import org.testng.Assert;
import org.testng.annotations.*;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.TestUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.constantsDao.ConstantsDao;
import ru.logistica.tms.dao.constantsDao.ConstantsDaoImpl;

import static org.testng.Assert.*;

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
        Integer prKey = supplierDao.save(supplier);
        ConnectionManager.getConnection().commit();
        supplier.setSupplierID(prKey);
        Assert.assertTrue(prKey == 1);
    }

    @Test(dependsOnMethods = {"testSave"})
    public void testGetById() throws Exception {
        Supplier supplier = supplierDao.getById(1);
        ConnectionManager.getConnection().commit();
        Assert.assertEquals(SupplierDaoImplTest.supplier, supplier);
    }


}