package ru.logistica.tms.dao;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.docDao.DocDao;
import ru.logistica.tms.dao.docDao.DocDaoImpl;
import ru.logistica.tms.dao.docPeriodDao.*;
import ru.logistica.tms.dao.orderDao.Order;
import ru.logistica.tms.dao.orderDao.OrderDao;
import ru.logistica.tms.dao.orderDao.OrderDaoImpl;
import ru.logistica.tms.dao.orderDao.OrderStatuses;
import ru.logistica.tms.dao.supplierDao.Supplier;
import ru.logistica.tms.dao.supplierDao.SupplierDao;
import ru.logistica.tms.dao.supplierDao.SupplierDaoImpl;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dao.warehouseDao.WarehouseDao;
import ru.logistica.tms.dao.warehouseDao.WarehouseDaoImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestRelations extends HibernateTest {
    public static final int BASE_PERIOD = 1800 * 1000;

    private Warehouse warehouse;
    private Doc doc;
    private DocPeriod emptyDocPeriod;
    private Supplier supplier;
    private DonutDocPeriod donutDocPeriod;
    private Order order;

    @BeforeClass
    public void setUp() throws Exception {
        super.setUp();
        // CREATE ENTITIES
        supplier = new Supplier();
        supplier.setInn("123456");

        warehouse = new Warehouse();
        warehouse.setWarehouseName("warehouse1");
        warehouse.setRusTimeZoneAbbr(RusTimeZoneAbbr.MSK);

        doc = new Doc();
        doc.setDocName("doc1");

        emptyDocPeriod = new DocPeriod();
        emptyDocPeriod.setPeriod(new Period(new Date(BASE_PERIOD *3), new Date(BASE_PERIOD *4)));

        donutDocPeriod = new DonutDocPeriod();
        donutDocPeriod.setPeriod(new Period(new Date(BASE_PERIOD *5), new Date(BASE_PERIOD *6)));
        donutDocPeriod.setCommentForDonut("donutComment");
        donutDocPeriod.setCreationDate(new Date(java.sql.Date.valueOf("2016-02-13").getTime()));
        donutDocPeriod.setDriver("someDriver");
        donutDocPeriod.setDriverPhoneNumber("865943567");
        donutDocPeriod.setPalletsQty((short) 4);
        donutDocPeriod.setLicensePlate("екх123");

        order = new Order();
        order.setBoxQty((short) 3);
        order.setOrderStatus(OrderStatuses.CREATED);
        order.setCommentForStatus("ffff");
        order.setOrderNumber("orderNumber1");


        // CREATE RELATIONS
        doc.setWarehouse(warehouse);
        Set<Doc> docs = new HashSet<>();
        docs.add(doc);
        warehouse.setDocs(docs);
        emptyDocPeriod.setDoc(doc);
        donutDocPeriod.setSupplier(supplier);
        donutDocPeriod.setDoc(doc);
        HashSet<DocPeriod> docPeriods = new HashSet<>();
        docPeriods.add(emptyDocPeriod);
        docPeriods.add(donutDocPeriod);
        doc.setDocPeriods(docPeriods);
        HashSet<DonutDocPeriod> donutDocPeriods = new HashSet<>();
        donutDocPeriods.add(donutDocPeriod);
        supplier.setDonutDocPeriods(donutDocPeriods);
        order.setFinalDestinationWarehouse(warehouse);
        order.setDonutDocPeriod(donutDocPeriod);
        Set<Order> orders = new HashSet<>();
        orders.add(order);
        donutDocPeriod.setOrders(orders);
        Set<Order> finalDestOrders = new HashSet<>();
        finalDestOrders.add(order);
        warehouse.setFinalDestinationOrders(finalDestOrders);
    }

    @Test
    public void testSaveAll() throws Exception {
        HibernateUtils.beginTransaction();
        SupplierDao supplierDao = new SupplierDaoImpl();
        supplierDao.persist(supplier);
        WarehouseDao warehouseDao = new WarehouseDaoImpl();
        warehouseDao.persist(warehouse);

        DocDao docDao = new DocDaoImpl();
        docDao.persist(doc);
        DocPeriodDao docPeriodDao = new DocPeriodImpl();
        docPeriodDao.persist(emptyDocPeriod);

        DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
        donutDocPeriodDao.persist(donutDocPeriod);

        OrderDao orderDao = new OrderDaoImpl();
        orderDao.persist(order);

        HibernateUtils.commitTransaction();
    }

}
