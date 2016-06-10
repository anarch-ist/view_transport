package ru.logistica.tms.dao;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.TestUtil;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.docPeriodDao.DocPeriod;
import ru.logistica.tms.dao.orderDao.OrderStatuses;
import ru.logistica.tms.dao.userDao.*;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dto.DocDateSelectorData;
import ru.logistica.tms.dto.DonutInsertData;
import ru.logistica.tms.util.UtcSimpleDateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DaoFacadeTest extends HibernateTest {
    private static final SimpleDateFormat dateFormat = new UtcSimpleDateFormat("yyyy-MM-dd"); //ISO-8601

    @BeforeClass
    public void setUp() throws Exception {
        super.setUp();
        TestUtil.fillWithSampleData();

    }

    @Test
    public void testGetAllWarehousesWithDocs() throws Exception {
        Set<Warehouse> allWarehousesWithDocs = DaoFacade.getAllWarehousesWithDocs();
        Assert.assertTrue(allWarehousesWithDocs.size() == 2);
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

    @Test
    public void testFillOffsetsForAbbreviations() throws Exception {
        DaoFacade.fillOffsetsForAbbreviations();
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.EET), 2.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.MSK), 3.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.SAMT), 4.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.YEKT), 5.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.OMST), 6.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.KRAT), 7.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.IRKT), 8.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.YAKT), 9.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.VLAT), 10.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.MAGT), 10.0);
        Assert.assertEquals(AppContextCache.timeZoneAbbrIntegerMap.get(RusTimeZoneAbbr.PETT), 12.0);
    }

    @Test(enabled = false)
    public void testCheckUser() throws Exception {

    }

    @Test(enabled = false)
    public void testGetAllPeriodsForDoc() throws Exception {
        String inputDateString = "2016-10-19";
        Date utcDate = dateFormat.parse(inputDateString);
        List<DocPeriod> allPeriodsForDoc = DaoFacade.getAllPeriodsForDoc(1, utcDate, utcDate);
        Assert.assertEquals(allPeriodsForDoc.size(), 3);
    }


    @Test
    public void testInsertDonut() throws Exception {
        DaoFacade.fillOffsetsForAbbreviations();

        Set<DonutInsertData.OrderInsertData> orders = new HashSet<>();
        DonutInsertData.OrderInsertData dtoOrder = new DonutInsertData.OrderInsertData("234", 2, 4, "erferg", OrderStatuses.CREATED.name());
        orders.add(dtoOrder);
        DonutInsertData donut = new DonutInsertData("450;510", "driver", "licPl", 4, "phN", "commne", orders);
        DocDateSelectorData docDateSelectorData = new DocDateSelectorData(new Date().getTime(), 1, 1);

        HibernateUtils.beginTransaction();
        SupplierUserDao supplierUserDao = new SupplierUserDaoImpl();
        SupplierUser user1 = supplierUserDao.findByLogin(SupplierUser.class, "user1");
        HibernateUtils.commitTransaction();


        DaoFacade.insertDonut(donut, docDateSelectorData, user1.getSupplier());

    }
}