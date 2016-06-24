package ru.logistica.tms.dao;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.dao.supplierDao.Supplier;
import ru.logistica.tms.dao.supplierDao.SupplierDao;
import ru.logistica.tms.dao.supplierDao.SupplierDaoImpl;

public class HsqlTest extends HibernateTest {

    @Test
    public void testTest() throws Exception {
        HibernateUtils.beginTransaction();
        Supplier suppliersEntity = new Supplier();
        suppliersEntity.setInn("1223445");
        SupplierDao suppliersDao = new SupplierDaoImpl();
        Integer generatedKey = suppliersDao.save(suppliersEntity);
        Assert.assertTrue(generatedKey == 1);
        HibernateUtils.commitTransaction();

    }
}
