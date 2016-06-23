package ru.logistica.tms.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.docDao.DocDao;
import ru.logistica.tms.dao.docDao.DocDaoImpl;
import ru.logistica.tms.dao.docPeriodDao.*;
import ru.logistica.tms.dao.orderDao.Order;
import ru.logistica.tms.dao.orderDao.OrderDao;
import ru.logistica.tms.dao.orderDao.OrderDaoImpl;
import ru.logistica.tms.dao.orderDao.OrderStatuses;
import ru.logistica.tms.dao.userDao.SupplierUser;
import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dao.userDao.UserDao;
import ru.logistica.tms.dao.userDao.UserDaoImpl;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dao.warehouseDao.WarehouseDao;
import ru.logistica.tms.dao.warehouseDao.WarehouseDaoImpl;
import ru.logistica.tms.dto.*;
import ru.logistica.tms.util.CriptUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DaoFacade {
    private static final Logger logger = LogManager.getLogger();

    private interface DaoScript {
        void execute() throws DAOException;
    }



    private static void doInTransaction(DaoScript daoScript) throws DaoScriptException {
        try {
            HibernateUtils.beginTransaction();
            daoScript.execute();
            HibernateUtils.commitTransaction();
        } catch (DAOException e) {
            logger.error(e.getMessage());
            HibernateUtils.rollbackTransaction();
            throw new DaoScriptException(e);
        } finally {
            HibernateUtils.getCurrentSession().close();
        }
    }

    private static void passUserIdToAuditTrigger(int userId) {
        Session currentSession = HibernateUtils.getCurrentSession();
        // BINDING ddl.sql audit.process_audit() SET LOCAL audit.current_user_id=userIdVal
        SQLQuery sqlQuery = currentSession.createSQLQuery("SET LOCAL audit.current_user_id=" + userId);
        sqlQuery.executeUpdate();
    }


    public static AuthResult checkUser(final String login, final String passMd5) throws DaoScriptException {
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

    public static DocDateSelectionForEmail getDocDateSelectionForEmail(final DocDateSelectorData docDateSelectorData) throws DaoScriptException {
        final DocDateSelectionForEmail result = new DocDateSelectionForEmail();
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                WarehouseDao warehouseDao = new WarehouseDaoImpl();
                Warehouse warehouse = warehouseDao.findById(Warehouse.class, docDateSelectorData.warehouseId);
                result.setWarehouseName(warehouse.getWarehouseName());
                DocDao docDao = new DocDaoImpl();
                Doc doc = docDao.findById(Doc.class, docDateSelectorData.docId);
                result.setDocName(doc.getDocName());
                result.setDate(new Date(docDateSelectorData.utcDate).toString());
            }
        });
        return result;
    }

    public static String getSupplierEmailByDonutDocPeriodId(final long donutDocPeriodId) throws DaoScriptException {
        final String[] result = new String[1];
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                result[0] = donutDocPeriod.getSupplierUser().getEmail();
            }
        });
        return result[0];
    }

    public static List<DocPeriod> getAllPeriodsForDoc(final Integer docId, final Date timeStampBegin, final Date timeStampEnd) throws DaoScriptException {
        final List<?>[] result = new List<?>[1];

        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DocPeriodDao docPeriodDao = new DocPeriodDaoImpl();
                List<DocPeriod> allPeriodsBetweenTimeStampsForDoc =
                        docPeriodDao.findAllPeriodsBetweenTimeStampsForDoc(docId, timeStampBegin, timeStampEnd);
                for (DocPeriod docPeriod : allPeriodsBetweenTimeStampsForDoc) {
                    if (docPeriod instanceof DonutDocPeriod) {
                        DonutDocPeriod donutDocPeriod = (DonutDocPeriod) docPeriod;
                        Hibernate.initialize(donutDocPeriod.getOrders());
                    }
                }
                result[0] = allPeriodsBetweenTimeStampsForDoc;
            }
        });
        return (List<DocPeriod>) result[0];
    }

    public static DonutDocPeriod getDonutWithRequests(final long donutDocPeriodId) throws DaoScriptException {
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

    public static Set<Warehouse> getAllWarehousesWithDocs() throws DaoScriptException {
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

    public static void fillOffsetsForAbbreviations() throws DaoScriptException {
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


    public static void openPeriods(final int userId, final OpenDocPeriodsData openDocPeriodsData) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                passUserIdToAuditTrigger(userId);
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

    public static void insertDocPeriods(final int userId, final PeriodsForInsertData periodsForInsertData, final int docId) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                passUserIdToAuditTrigger(userId);
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

    public static void updateDonutWithRequests(final int userId, final DonutUpdateData donutUpdateData) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                passUserIdToAuditTrigger(userId);
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
                    Order order = orderDao.findById(Order.class, orderIdForDelete);
                    order.setOrderId(orderIdForDelete);
                    orderDao.delete(order);
                }

            }

        });
    }

    public static void deleteDonutWithRequests(final int userId, final long donutDocPeriodId) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                passUserIdToAuditTrigger(userId);
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                OrderDao orderDao = new OrderDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                Set<Order> orders = donutDocPeriod.getOrders();
                for (Order order : orders) {
                    orderDao.delete(order);
                }
                donutDocPeriodDao.delete(donutDocPeriod);
            }
        });

    }

    public static void deleteDonutWithRequestsIfCreated(final int userId, final long donutDocPeriodId) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                passUserIdToAuditTrigger(userId);
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                OrderDao orderDao = new OrderDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                Set<Order> orders = donutDocPeriod.getOrders();
                for (Order order : orders) {
                    if (order.getOrderStatus() != OrderStatuses.CREATED)
                        throw new DAOException("У одной из заявок статус отличается от " + OrderStatuses.CREATED.name());
                }
                for (Order order : orders) {
                    orderDao.delete(order);
                }
                donutDocPeriodDao.delete(donutDocPeriod);
            }
        });

    }

    public static void insertDonut(final int userId, final DonutInsertData donutInsertData, final DocDateSelectorData docDateSelectorData, final SupplierUser supplierUser) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                passUserIdToAuditTrigger(userId);
                DocDao docDao = new DocDaoImpl();
                Doc doc = docDao.findById(Doc.class, docDateSelectorData.docId);
                WarehouseDao warehouseDao = new WarehouseDaoImpl();

                DonutDocPeriod donutDocPeriod = new DonutDocPeriod();

                donutDocPeriod.setDoc(doc);
                donutDocPeriod.setSupplierUser(supplierUser);
                donutDocPeriod.setPalletsQty((short) donutInsertData.palletsQty);
                donutDocPeriod.setLicensePlate(donutInsertData.licensePlate);
                donutDocPeriod.setCommentForDonut(donutInsertData.commentForDonut);
                donutDocPeriod.setCreationDate(new Date());
                donutDocPeriod.setDriver(donutInsertData.driver);
                donutDocPeriod.setDriverPhoneNumber(donutInsertData.driverPhoneNumber);
                donutDocPeriod.setPeriod(new Period(new Date(donutInsertData.periodBegin), new Date(donutInsertData.periodEnd)));
                donutDocPeriod.setDoc(doc);

                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                donutDocPeriodDao.save(donutDocPeriod);

                OrderDao orderDao = new OrderDaoImpl();
                for (DonutInsertData.OrderInsertData dtoOrder: donutInsertData.orders) {
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
        });
    }

}
