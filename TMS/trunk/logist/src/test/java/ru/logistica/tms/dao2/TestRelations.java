package ru.logistica.tms.dao2;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.dao2.docDao.Doc;
import ru.logistica.tms.dao2.docDao.DocDao;
import ru.logistica.tms.dao2.docDao.DocDaoImpl;
import ru.logistica.tms.dao2.docPeriodDao.*;
import ru.logistica.tms.dao2.orderDao.Order;
import ru.logistica.tms.dao2.orderDao.OrderDao;
import ru.logistica.tms.dao2.orderDao.OrderDaoImpl;
import ru.logistica.tms.dao2.orderDao.OrderStatuses;
import ru.logistica.tms.dao2.supplierDao.Supplier;
import ru.logistica.tms.dao2.supplierDao.SupplierDao;
import ru.logistica.tms.dao2.supplierDao.SupplierDaoImpl;
import ru.logistica.tms.dao2.warehouseDao.Warehouse;
import ru.logistica.tms.dao2.warehouseDao.WarehouseDao;
import ru.logistica.tms.dao2.warehouseDao.WarehouseDaoImpl;

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

    @BeforeMethod
    private void createAllBeans() {

        // CREATE ENTITIES
        supplier = new Supplier();
        supplier.setInn("123456");

        warehouse = new Warehouse();
        warehouse.setWarehouseName("warehouse1");

        doc = new Doc();
        doc.setDocName("doc1");

        emptyDocPeriod = new DocPeriod();
        emptyDocPeriod.setPeriodBegin(new Date(BASE_PERIOD *3));
        emptyDocPeriod.setPeriodEnd(new Date(BASE_PERIOD *4));

        donutDocPeriod = new DonutDocPeriod();
        donutDocPeriod.setPeriodBegin(new Date(BASE_PERIOD *5));
        donutDocPeriod.setPeriodEnd(new Date(BASE_PERIOD *6));
        donutDocPeriod.setComment("donutComment");
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
