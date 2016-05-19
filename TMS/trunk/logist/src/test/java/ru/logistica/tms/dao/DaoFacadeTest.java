package ru.logistica.tms.dao;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.TestUtil;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.warehouseDao.Warehouse;

import java.util.Set;

public class DaoFacadeTest extends HibernateTest {

    @BeforeClass
    public void setUp() throws Exception {
        super.setUp();
        TestUtil.fillWithSampleData();
    }

    @Test
    public void testGetAllWarehousesWithDocs() throws Exception {
        Set<Warehouse> allWarehousesWithDocs = DaoFacade.getAllWarehousesWithDocs();
        Assert.assertTrue(allWarehousesWithDocs.size() == 1);
        for (Warehouse warehouse : allWarehousesWithDocs) {
            if (warehouse.getWarehouseId() == 1) {
                Set<Doc> docs = warehouse.getDocs();
                Assert.assertTrue(docs.size() == 3);
            }
        }
    }

    @Test
    public void testGetAllDocsForWarehouseUserId() throws Exception {
        //TODO
//        Set<Warehouse> allWarehousesWithDocs = DaoFacade.getAllDocsForWarehouseUserId();
//        Assert.assertTrue(allWarehousesWithDocs.size() == 1);
//        for (Warehouse warehouse : allWarehousesWithDocs) {
//            if (warehouse.getWarehouseId() == 1) {
//                Set<Doc> docs = warehouse.getDocs();
//                Assert.assertTrue(docs.size() == 3);
//            }
//        }
    }


    @Test(enabled = false)
    public void testCheckUser() throws Exception {

    }
}