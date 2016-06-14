package ru.logistica.tms.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import ru.logistica.tms.dao.cache.AppContextCache;
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
import ru.logistica.tms.dao.userDao.*;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dao.warehouseDao.WarehouseDao;
import ru.logistica.tms.dao.warehouseDao.WarehouseDaoImpl;
import ru.logistica.tms.dto.*;
import ru.logistica.tms.util.CriptUtils;

import java.util.*;

public class DaoFacade {
    private static final Logger logger = LogManager.getLogger();



    private interface DaoScript {
        void execute() throws DAOException;
    }

    private static void doInTransaction(DaoScript daoScript) {
        try {
            HibernateUtils.beginTransaction();
            daoScript.execute();
            HibernateUtils.commitTransaction();
        } catch (DAOException e) {
            logger.error(e.getMessage());
            HibernateUtils.rollbackTransaction();
        } finally {
            HibernateUtils.getCurrentSession().close();
        }
    }
    // TODO
//    public static String getSupplierEmailByDonutDocPeriodId(final long donutDocPeriodId) {
////        String result;
////        doInTransaction(new DaoScript() {
////            @Override
////            public void execute() throws DAOException {
////                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
////                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
////                donutDocPeriod.getSupplier().getEmail();
////
////            }
////        });
//    }

    public static void openPeriods(final OpenDocPeriodsData openDocPeriodsData) {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DocPeriodDao docPeriodDao = new DocPeriodDaoImpl();
                DocDao docDao = new DocDaoImpl();

                for (OpenDocPeriodsData.DocAction docAction : openDocPeriodsData) {

                    if (docAction.idOperation instanceof OpenDocPeriodsData.DocAction.DeleteOperation) {
                        docPeriodDao.delete(docPeriodDao.findById(DocPeriod.class, docAction.idOperation.docPeriodId));
                    } else if (docAction.idOperation instanceof OpenDocPeriodsData.DocAction.UpdateOperation) {
                        OpenDocPeriodsData.DocAction.UpdateOperation updateOperation = (OpenDocPeriodsData.DocAction.UpdateOperation) docAction.idOperation;
                        DocPeriod docPeriod = docPeriodDao.findById(DocPeriod.class, docAction.idOperation.docPeriodId);
                        docPeriod.setPeriod(new Period(new Date(updateOperation.periodBegin), new Date(updateOperation.periodEnd)));
                        docPeriodDao.update(docPeriod);
                    }

                    Set<OpenDocPeriodsData.DocAction.InsertOperation> insertOperations = docAction.insertOperations;
                    for (OpenDocPeriodsData.DocAction.InsertOperation insertOperation : insertOperations) {
                        DocPeriod docPeriod = new DocPeriod();
                        docPeriod.setPeriod(new Period(new Date(insertOperation.periodBegin), new Date(insertOperation.periodEnd)));
                        docPeriod.setDoc(docDao.findById(Doc.class, insertOperation.docId));
                        docPeriodDao.save(docPeriod);
                    }

                }
            }
        });
    }

    public static void insertDocPeriods(final PeriodsForInsertData periodsForInsertData, final int docId) {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DocPeriodDao docPeriodDao = new DocPeriodDaoImpl();
                DocDao docDao = new DocDaoImpl();
                Doc doc = docDao.findById(Doc.class, docId);
                for (PeriodsForInsertData.PeriodData periodData : periodsForInsertData) {
                    DocPeriod docPeriod = new DocPeriod();
                    docPeriod.setPeriod(new Period(periodData.periodBegin, periodData.periodEnd));
                    docPeriod.setDoc(doc);
                    docPeriodDao.save(docPeriod);
                }
            }
        });

    }

    public static DonutDocPeriod selectDonutWithRequests(final long donutDocPeriodId) {
        final DonutDocPeriod[] result = new DonutDocPeriod[1];
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                Hibernate.initialize(donutDocPeriod.getOrders());
                result[0] = donutDocPeriod;
            }
        });
        return result[0];
    }

    public static void updateDonutWithRequests(final DonutUpdateData donutUpdateData) {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, (long) donutUpdateData.donutDocPeriodId);
                donutDocPeriod.setDriverPhoneNumber(donutUpdateData.driverPhoneNumber);
                donutDocPeriod.setDriver(donutUpdateData.driver);
                donutDocPeriod.setCommentForDonut(donutUpdateData.commentForDonut);
                donutDocPeriod.setLicensePlate(donutUpdateData.licensePlate);
                donutDocPeriod.setPalletsQty((short) donutUpdateData.palletsQty);


                OrderDao orderDao = new OrderDaoImpl();
                WarehouseDao warehouseDao = new WarehouseDaoImpl();
                Set<DonutUpdateData.OrderUpdateData> ordersUpdateData = donutUpdateData.orders;
                for (DonutUpdateData.OrderUpdateData orderUpdateData : ordersUpdateData) {
                    if (orderUpdateData.orderId == null) {
                        // insert
                        Order order = new Order();
                        order.setOrderStatus(OrderStatuses.valueOf(orderUpdateData.orderStatusId));
                        order.setOrderNumber(orderUpdateData.orderNumber);
                        order.setFinalDestinationWarehouse(warehouseDao.findById(Warehouse.class, orderUpdateData.finalDestinationWarehouseId));
                        order.setBoxQty((short) orderUpdateData.boxQty);
                        order.setDonutDocPeriod(donutDocPeriod);
                        order.setCommentForStatus(orderUpdateData.commentForStatus);
                        orderDao.save(order);
                    } else {
                        // update
                        Order order = orderDao.findById(Order.class, orderUpdateData.orderId);
                        order.setOrderStatus(OrderStatuses.valueOf(orderUpdateData.orderStatusId));
                        order.setOrderNumber(orderUpdateData.orderNumber);
                        order.setFinalDestinationWarehouse(warehouseDao.findById(Warehouse.class, orderUpdateData.finalDestinationWarehouseId));
                        order.setBoxQty((short) orderUpdateData.boxQty);
                        order.setCommentForStatus(orderUpdateData.commentForStatus);
                        orderDao.update(order);
                    }
                }
                donutDocPeriodDao.update(donutDocPeriod);

                for (Integer orderIdForDelete: donutUpdateData.ordersIdForDelete) {
                    // delete transient instance
                    Order order = orderDao.findById(Order.class, orderIdForDelete);
                    order.setOrderId(orderIdForDelete);
                    orderDao.delete(order);
                }

            }

        });
    }

    public static void deleteDonutWithRequests(final long donutDocPeriodId) {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                Set<Order> orders = donutDocPeriod.getOrders();
                for (Order order : orders) {
                    OrderDao orderDao = new OrderDaoImpl();
                    orderDao.delete(order);
                }
                donutDocPeriodDao.delete(donutDocPeriod);
            }
        });

    }

    public static void insertDonut(final DonutInsertData donut, final DocDateSelectorData docDateSelectorData, final Supplier usersSupplier) {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DocDao docDao = new DocDaoImpl();
                Doc doc = docDao.findById(Doc.class, docDateSelectorData.docId);
                SupplierDao supplierDao = new SupplierDaoImpl();
                Supplier supplier = supplierDao.findById(Supplier.class, usersSupplier.getSupplierId());
                WarehouseDao warehouseDao = new WarehouseDaoImpl();

                DonutDocPeriod donutDocPeriod = new DonutDocPeriod();

                donutDocPeriod.setDoc(doc);
                donutDocPeriod.setSupplier(supplier);
                donutDocPeriod.setPalletsQty((short) donut.palletsQty);
                donutDocPeriod.setLicensePlate(donut.licensePlate);
                donutDocPeriod.setCommentForDonut(donut.commentForDonut);
                donutDocPeriod.setCreationDate(new Date());
                donutDocPeriod.setDriver(donut.driver);
                donutDocPeriod.setDriverPhoneNumber(donut.driverPhoneNumber);
                donutDocPeriod.setPeriod(getPeriod());
                donutDocPeriod.setDoc(doc);

                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                donutDocPeriodDao.save(donutDocPeriod);

                OrderDao orderDao = new OrderDaoImpl();
                for (DonutInsertData.OrderInsertData dtoOrder: donut.orders) {
                    Order order = new Order();
                    order.setBoxQty((short) dtoOrder.boxQty);
                    order.setCommentForStatus(dtoOrder.commentForStatus);
                    order.setFinalDestinationWarehouse(warehouseDao.findById(Warehouse.class, dtoOrder.finalDestinationWarehouseId));
                    order.setOrderNumber(dtoOrder.orderNumber);
                    order.setOrderStatus(OrderStatuses.valueOf(dtoOrder.orderStatusId));
                    order.setDonutDocPeriod(donutDocPeriod);
                    orderDao.save(order);
                }
            }

            private Period getPeriod() throws DAOException {
                // TODO redo this shit
                String[] split = donut.period.split(";");
                int periodBegin = Integer.parseInt(split[0]); // in minutes from day begin
                int periodEnd = Integer.parseInt(split[1]);  // in minutes from day begin
                long dateBegin = docDateSelectorData.utcDate;
                long timeStampBegin = dateBegin + periodBegin * 60 * 1000;
                long timeStampEnd = dateBegin + periodEnd * 60 * 1000;
                return new Period(new Date(timeStampBegin), new Date(timeStampEnd));
            }
        });
    }

    public static Set<Warehouse> getAllWarehousesWithDocs() {
        final Set<Warehouse> result = new HashSet<>();
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                WarehouseDao warehouseDao = new WarehouseDaoImpl();
                List<Warehouse> warehouses = warehouseDao.findAll(Warehouse.class);
                for (Warehouse warehouse : warehouses) {
                    result.add(warehouse);
                }
            }
        });
        return result;
    }

    public static AuthResult checkUser(final String login, final String passMd5) {
        final AuthResult authResult = new AuthResult();
        // md5(md5(pass)+salt)
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                UserDao userDao = new UserDaoImpl();
                User user = userDao.findByLogin(User.class, login);
                if (user == null) {
                    authResult.setNoSuchLogin();
                } else if (!user.getPassAndSalt().equals(CriptUtils.md5(passMd5 + user.getSalt())))
                    authResult.setNoSuchPassword();
                else {
                    authResult.setAuthSuccess();
                    authResult.setUser(user);
                }
            }
        });
        return authResult;
    }

    public static void fillOffsetsForAbbreviations() {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                RusTimeZoneAbbr[] rusTimeZoneAbbrs = RusTimeZoneAbbr.values();
                WarehouseDao warehouseDao = new WarehouseDaoImpl();
                for (RusTimeZoneAbbr value : rusTimeZoneAbbrs) {
                    AppContextCache.timeZoneAbbrIntegerMap.put(value, warehouseDao.findOffsetByAbbreviation(value));
                }
                warehouseDao.findAll(Warehouse.class);
            }
        });
    }

    public static List<DocPeriod> getAllPeriodsForDoc(final Integer docId, final Date timeStampBegin, final Date timeStampEnd) {
        final List<?>[] result = new List<?>[1];

        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DocPeriodDao docPeriodDao = new DocPeriodDaoImpl();
                List<DocPeriod> allPeriodsBetweenTimeStampsForDoc =
                        docPeriodDao.findAllPeriodsBetweenTimeStampsForDoc(docId, timeStampBegin, timeStampEnd);
                result[0] = allPeriodsBetweenTimeStampsForDoc;
            }
        });
        return (List<DocPeriod>) result[0];
    }

}
