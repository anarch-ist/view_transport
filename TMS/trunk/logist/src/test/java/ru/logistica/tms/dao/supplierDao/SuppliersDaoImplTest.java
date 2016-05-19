package ru.logistica.tms.dao.supplierDao;

import org.testng.Assert;
import ru.logistica.tms.HibernateStandardTest;

import java.util.List;

public class SuppliersDaoImplTest extends HibernateStandardTest{

    @Override
    protected void testSaveImpl() throws Exception {
        Supplier suppliersEntity = new Supplier();
        suppliersEntity.setInn("1223445");
        SupplierDao suppliersDao = new SupplierDaoImpl();
        Integer generatedKey = suppliersDao.save(suppliersEntity);
        Assert.assertTrue(generatedKey == 1);
    }

    @Override
    protected void testFindByIdImpl() throws Exception {
        SupplierDao suppliersDao = new SupplierDaoImpl();
        Supplier supplier = suppliersDao.findById(Supplier.class, 1);
        Assert.assertEquals(supplier.getInn(), "1223445");
        Assert.assertEquals(supplier.getSupplierId().intValue(), 1);
    }

    @Override
    protected void testUpdateImpl() throws Exception {
        SupplierDao suppliersDao = new SupplierDaoImpl();
        Supplier suppliersEntity = new Supplier();
        suppliersEntity.setInn("11111111");
        suppliersEntity.setSupplierId(1);
        suppliersDao.update(suppliersEntity);
        Supplier updatedSupplier = suppliersDao.findById(Supplier.class, 1);
        Assert.assertEquals(updatedSupplier, suppliersEntity);
    }

    @Override
    protected void testFindAllImpl() throws Exception {
        SupplierDao suppliersDao = new SupplierDaoImpl();
        List<Supplier> suppliers = suppliersDao.findAll(Supplier.class);
        Assert.assertEquals(suppliers.size(), 1);
    }

    @Override
    protected void testDeleteImpl() throws Exception {
        SupplierDao suppliersDao = new SupplierDaoImpl();
        Supplier forDelete = suppliersDao.findAll(Supplier.class).get(0);
        suppliersDao.delete(forDelete);
        List<Supplier> suppliers = suppliersDao.findAll(Supplier.class);
        Assert.assertEquals(suppliers.size(), 0);
    }

}