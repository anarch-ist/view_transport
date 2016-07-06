package ru.logistica.tms.dao;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.TestUtil;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.orderDao.OrderStatuses;
import ru.logistica.tms.dao.userDao.*;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dto.*;
import ru.logistica.tms.util.UtcSimpleDateFormat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DaoFacadeTest extends HibernateTest {
    private static final SimpleDateFormat dateFormat = new UtcSimpleDateFormat("yyyy-MM-dd"); //ISO-8601

    @BeforeMethod
    public void beforeMethod() throws Exception {
        TestUtil.jdbcRecreateAndTestInserts();
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

    @Test
    public void testInsertDonut() throws Exception {
        DaoFacade.fillOffsetsForAbbreviations();

        Set<OrderData> orders = new HashSet<>();
        OrderData dtoOrder = new OrderData(null, "234", 2, 4, "erferg", OrderStatuses.CREATED.name(), "inv1", new BigDecimal(12.0), 4);
        orders.add(dtoOrder);
        DonutData donut = new DonutData(null, 450*60*1000, 510*60*1000, "driver", "licPl", 4, "phN", "commne", orders, null);
        DocDateSelectorData docDateSelectorData = new DocDateSelectorData(new Date().getTime(), 1, 1);

        HibernateUtils.beginTransaction();
        SupplierUserDao supplierUserDao = new SupplierUserDaoImpl();
        SupplierUser user1 = supplierUserDao.findByLogin(SupplierUser.class, "us1");
        HibernateUtils.commitTransaction();

        DaoFacade.insertDonut(1, donut, docDateSelectorData, user1);

    }

    @Test(expectedExceptions = {DaoScriptException.class}, expectedExceptionsMessageRegExp = ".*Период.*занят частично или полностью.")
    public void testOpenPeriodsSaveExistingPeriod() throws Exception {
        OpenDocPeriodsData openDocPeriodsData = new OpenDocPeriodsData();

        Set<OpenDocPeriodsData.DocAction.InsertOperation> insertOperations = new HashSet<>();
        OpenDocPeriodsData.DocAction.InsertOperation insertOperation = new OpenDocPeriodsData.DocAction.InsertOperation(1, 1476871200000L, 1476873000000L);
        insertOperations.add(insertOperation);

        OpenDocPeriodsData.DocAction docAction = new OpenDocPeriodsData.DocAction(null, insertOperations);
        openDocPeriodsData.add(docAction);
        DaoFacade.openPeriods(null, openDocPeriodsData);
    }

    @Test(expectedExceptions = {DaoScriptException.class}, expectedExceptionsMessageRegExp = "Данные изменились, операция не актуальна на выбранных данных.")
    public void testOpenPeriodsDeleteNotExistingPeriod() throws Exception {
        OpenDocPeriodsData openDocPeriodsData = new OpenDocPeriodsData();
        OpenDocPeriodsData.DocAction docAction = new OpenDocPeriodsData.DocAction(new OpenDocPeriodsData.DocAction.DeleteOperation(200), null);
        openDocPeriodsData.add(docAction);
        DaoFacade.openPeriods(null, openDocPeriodsData);
    }

    @Test
    public void testGetAllDonutsForSupplier() throws Exception {
        SupplierDonuts allDonutsForSupplier = DaoFacade.getAllDonutsForSupplier(1, "<br/>");
        System.out.println(allDonutsForSupplier);
    }

}