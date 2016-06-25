package ru.logistica.tms.dao.supplierDao;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.dao.HibernateUtils;

import java.util.List;

public class SuppliersDaoImplTest extends HibernateTest {

    @Test
    protected void testSaveImpl() throws Exception {
        HibernateUtils.beginTransaction();
        Supplier suppliersEntity = new Supplier();
        suppliersEntity.setInn("1223445");
        SupplierDao suppliersDao = new SupplierDaoImpl();
        Integer generatedKey = suppliersDao.save(suppliersEntity);
        Assert.assertTrue(generatedKey == 1);
        HibernateUtils.commitTransaction();
    }

    @Test(dependsOnMethods = {"testSaveImpl"})
    protected void testFindByIdImpl() throws Exception {
        HibernateUtils.beginTransaction();
        SupplierDao suppliersDao = new SupplierDaoImpl();
        Supplier supplier = suppliersDao.findById(Supplier.class, 1);
        Assert.assertEquals(supplier.getInn(), "1223445");
        Assert.assertEquals(supplier.getSupplierId().intValue(), 1);
        HibernateUtils.commitTransaction();
    }

    @Test(dependsOnMethods = {"testFindByIdImpl"})
    protected void testUpdateImpl() throws Exception {
        HibernateUtils.beginTransaction();
        SupplierDao suppliersDao = new SupplierDaoImpl();
        Supplier suppliersEntity = new Supplier();
        suppliersEntity.setInn("11111111");
        suppliersEntity.setSupplierId(1);
        suppliersDao.update(suppliersEntity);
        Supplier updatedSupplier = suppliersDao.findById(Supplier.class, 1);
        Assert.assertEquals(updatedSupplier, suppliersEntity);
        HibernateUtils.commitTransaction();
    }

    @Test(dependsOnMethods = {"testFindByIdImpl"})
    protected void testFindAllImpl() throws Exception {
        HibernateUtils.beginTransaction();
        SupplierDao suppliersDao = new SupplierDaoImpl();
        List<Supplier> suppliers = suppliersDao.findAll(Supplier.class);
        Assert.assertEquals(suppliers.size(), 1);
        HibernateUtils.commitTransaction();
    }

    @Test(dependsOnMethods = {"testFindAllImpl"})
    protected void testDeleteImpl() throws Exception {
        HibernateUtils.beginTransaction();
        SupplierDao suppliersDao = new SupplierDaoImpl();
        Supplier forDelete = suppliersDao.findAll(Supplier.class).get(0);
        suppliersDao.delete(forDelete);
        List<Supplier> suppliers = suppliersDao.findAll(Supplier.class);
        Assert.assertEquals(suppliers.size(), 0);
        HibernateUtils.commitTransaction();
    }

}