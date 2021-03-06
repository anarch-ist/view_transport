package ru.logistica.tms.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
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
import ru.logistica.tms.dao.userDao.*;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dao.warehouseDao.WarehouseDao;
import ru.logistica.tms.dao.warehouseDao.WarehouseDaoImpl;
import ru.logistica.tms.dto.*;
import ru.logistica.tms.util.CriptUtils;
import ru.logistica.tms.util.RusNames;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

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
        } catch (Exception e) {
            logger.error(e);
            HibernateUtils.rollbackTransaction();
            String message = e.getMessage();
            if (e instanceof org.hibernate.exception.GenericJDBCException) {
                message = e.getCause().getMessage();
            }
            throw new DaoScriptException(message, e);
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

    public static Set<Warehouse> getAllWarehouses() throws DaoScriptException {
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

    public static Warehouse getWarehouseWithDocs(final int warehouseId) throws DaoScriptException {
        final Warehouse[] result = new Warehouse[1];
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                WarehouseDao warehouseDao = new WarehouseDaoImpl();
                result[0] = warehouseDao.findById(Warehouse.class, warehouseId);
                Hibernate.initialize(result[0].getDocs());
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
                    Hibernate.initialize(warehouse.getDocs());
                    result.add(warehouse);
                }
            }
        });
        return result;
    }

    public static SupplierDonuts getAllDonutsForSupplier(final long donutDocPeriodId, final String delimiter) throws DaoScriptException {

        final SupplierDonuts[] result = new SupplierDonuts[1];
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                final Double timeShift = AppContextCache.timeZoneAbbrIntegerMap.get(donutDocPeriod.getDoc().getWarehouse().getRusTimeZoneAbbr());
                Supplier supplier = donutDocPeriod.getSupplierUser().getSupplier();
                final int supplierId = supplier.getSupplierId();

                Session currentSession = HibernateUtils.getCurrentSession();
                Query query = currentSession.createSQLQuery(
                        "SELECT\n" +
                                "  doc_periods.periodBegin,\n" +
                                "  doc_periods.periodEnd,\n" +
                                "  warehouses.warehousename,\n" +
                                "  docs.docname,\n" +
                                "  string_agg(orders.orderNumber, :_delimiter) AS orderNumbers,\n" +
                                "  string_agg(orders.orderstatus, :_delimiter) AS orderStatuses,\n" +
                                "  donut_doc_periods.commentfordonut,\n" +
                                "  GREATEST(donut_doc_periods.lastModified, max(orders.lastmodified))\n" +
                                "FROM donut_doc_periods\n" +
                                "  INNER JOIN doc_periods ON donut_doc_periods.donutdocperiodid = doc_periods.docperiodid\n" +
                                "  INNER JOIN docs ON doc_periods.docid = docs.docid\n" +
                                "  INNER JOIN warehouses ON docs.warehouseid = warehouses.warehouseid\n" +
                                "  LEFT JOIN orders ON donut_doc_periods.donutdocperiodid = orders.donutdocperiodid\n" +
                                "  INNER JOIN supplier_users ON donut_doc_periods.supplieruserid = supplier_users.userid\n" +
                                "  INNER JOIN suppliers ON supplier_users.supplierid = suppliers.supplierid\n" +
                                "WHERE suppliers.supplierid = :supplierId\n" +
                                "GROUP BY donut_doc_periods.donutdocperiodid, doc_periods.periodBegin, doc_periods.periodEnd, docs.docname,\n" +
                                "  warehouses.warehousename, donut_doc_periods.lastModified\n" +
                                "ORDER BY donut_doc_periods.lastModified DESC\n" +
                                "LIMIT 1000;"
                );
                query.setInteger("supplierId", supplierId);
                query.setString("_delimiter", delimiter);
                query.setReadOnly(true);
                query.setResultTransformer(new ResultTransformer() {
                    @Override
                    public Object transformTuple(Object[] rowData, String[] aliasNames) {
                        SupplierDonuts.SupplierDonut donutForSupplier = new SupplierDonuts.SupplierDonut();
                        donutForSupplier.setPeriodBegin(getDateWithShift((Date) rowData[0]));
                        donutForSupplier.setPeriodEnd(getDateWithShift((Date) rowData[1]));
                        donutForSupplier.setWarehouseName((String) rowData[2]);
                        donutForSupplier.setDocName((String) rowData[3]);
                        donutForSupplier.setOrderNumbersAsString((String) rowData[4]);
                        String orderStatusesAsString = rowData[5] == null ? "NO_ORDERS" : (String)rowData[5];
                        String[] orderStatuses = orderStatusesAsString.split(delimiter);
                        String orderStatusesLocalized = "";
                        for (int i = 0; i < orderStatuses.length; i++) {
                            orderStatusesLocalized += RusNames.getOrderStatusesConverter().get(OrderStatuses.valueOf(orderStatuses[i]));
                            if (i != orderStatuses.length - 1) {
                                orderStatusesLocalized += delimiter;
                            }
                        }
                        donutForSupplier.setOrderStatusesAsString(orderStatusesLocalized);
                        donutForSupplier.setComment((String) rowData[6]);
                        donutForSupplier.setLastModified((Date) rowData[7]);
                        return donutForSupplier;
                    }

                    private Date getDateWithShift(Date date) {
                        date.setTime(date.getTime() + 3600 * 1000 * timeShift.intValue());
                        return date;
                    }

                    @Override
                    public List transformList(List collection) {
                        SupplierDonuts result = new SupplierDonuts();
                        result.addAll(collection);
                        return result;
                    }
                });
                result[0] = (SupplierDonuts) query.list();
                result[0].setSupplierName(supplier.getInn());
            }
        });
        return result[0];
    }


    public static DocReports getDocReportsForWarehouse(final Date periodBegin, final Date periodEnd, final int warehouseId,final String delimiter) throws DaoScriptException {
        final DocReports[] result = new DocReports[1];
        try{
            doInTransaction(new DaoScript() {
                @Override
                public void execute() throws DAOException {
                    WarehouseDaoImpl warehouseDao = new WarehouseDaoImpl();
                    Warehouse warehouse = (Warehouse) warehouseDao.findById(Warehouse.class, warehouseId);

                    final Double timeShift = (Double) AppContextCache.timeZoneAbbrIntegerMap.get(warehouse.getRusTimeZoneAbbr());
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    Session currentSession = HibernateUtils.getCurrentSession();
                    SQLQuery query = currentSession.createSQLQuery("SELECT\n  suppliers.inn,\n  doc_periods.periodbegin,\n  docs.docname,\n  donut_doc_periods.licenseplate as licensePlate,\n  doc_periods.periodend,\n  string_agg((SELECT warehouses.warehousename FROM warehouses WHERE orders.finaldestinationwarehouseid = warehouses.warehouseid), :_delimiter) AS FinalDestinations,\n  string_agg(cast(orders.orderpalletsqty AS CHARACTER VARYING), :_delimiter) AS orderPalletsQty,\n  string_agg(cast(orders.boxqty AS CHARACTER VARYING), :_delimiter) AS boxQty\n\nFROM donut_doc_periods\n  INNER JOIN doc_periods ON donut_doc_periods.donutdocperiodid = doc_periods.docperiodid\n  INNER JOIN docs ON doc_periods.docid = docs.docid\n\n  LEFT JOIN orders ON donut_doc_periods.donutdocperiodid = orders.donutdocperiodid\n  INNER JOIN warehouses ON docs.warehouseid = warehouses.warehouseid\n  INNER JOIN supplier_users ON donut_doc_periods.supplieruserid = supplier_users.userid\n  INNER JOIN suppliers ON supplier_users.supplierid = suppliers.supplierid\nWHERE  warehouses.warehouseid= :warehouseid AND doc_periods.periodbegin BETWEEN cast(:periodbegin AS TIMESTAMP) - INTERVAL '3 hours' AND cast(:periodend AS DATE)+1GROUP BY donut_doc_periods.donutdocperiodid, doc_periods.periodBegin, doc_periods.periodEnd, docs.docname,\n   donut_doc_periods.lastModified, suppliers.inn\nORDER BY doc_periods.periodbegin ASC\nLIMIT 1000;");
                    query.setInteger("warehouseid", warehouseId);
                    periodEnd.setTime(periodEnd.getTime() + (long) (3600000 * timeShift.intValue()));
                    query.setParameter("periodbegin", periodBegin);
                    query.setParameter("periodend", periodEnd);
                    query.setString("_delimiter", delimiter);
                    query.setReadOnly(true);
                    query.setResultTransformer(new ResultTransformer() {

                        public DocReports.DocReport transformTuple(Object[] rowData, String[] aliasNames) {
                            DocReports.DocReport docReport = new DocReports.DocReport();
                            docReport.setInn((String) rowData[0]);
                            docReport.setDate(dateFormat.format(this.getDateWithShift((Date) rowData[1])));
                            docReport.setDocname((String) rowData[2]);
                            docReport.setLicenseplate((String) rowData[3]);
                            docReport.setPeriod(timeFormat.format((Date) rowData[1]) + "-" + timeFormat.format(this.getDateWithShift((Date) rowData[4])));
                            docReport.setFinaldestinations((String) rowData[5]);
                            docReport.setOrderpalletsqty((String) rowData[6]);
                            docReport.setBoxqty((String) rowData[7]);
                            return docReport;
                        }

                        private Date getDateWithShift(Date date) {
                            date.setTime(date.getTime() + (long) (3600000 * timeShift.intValue()));
                            return date;
                        }

                        public List transformList(List collection) {
                            DocReports result = new DocReports();
                            result.addAll(collection);
                            return result;
                        }
                    });
                    result[0] = (DocReports) query.list();
                    result[0].setWarehouseName(warehouse.getWarehouseName());

                }

            });
        } catch (DaoScriptException e){
            e.printStackTrace();
        }
        return result[0];
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


    public static void openPeriods(final Integer userId, final OpenDocPeriodsData openDocPeriodsData) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                if (userId != null) {
                    passUserIdToAuditTrigger(userId);
                }

                DocPeriodDao docPeriodDao = new DocPeriodDaoImpl();
                DocDao docDao = new DocDaoImpl();

                for (OpenDocPeriodsData.DocAction docAction : openDocPeriodsData) {

                    if (docAction.idOperation instanceof OpenDocPeriodsData.DocAction.DeleteOperation) {
                        DocPeriod docPeriod = docPeriodDao.findById(DocPeriod.class, docAction.idOperation.docPeriodId);
                        docPeriodDao.delete(docPeriod);
                    } else if (docAction.idOperation instanceof OpenDocPeriodsData.DocAction.UpdateOperation) {
                        OpenDocPeriodsData.DocAction.UpdateOperation updateOperation = (OpenDocPeriodsData.DocAction.UpdateOperation) docAction.idOperation;
                        DocPeriod docPeriod = docPeriodDao.findById(DocPeriod.class, docAction.idOperation.docPeriodId);
                        docPeriod.setPeriod(new Period(new Date(updateOperation.periodBegin), new Date(updateOperation.periodEnd)));
                        docPeriodDao.update(docPeriod);
                        HibernateUtils.getCurrentSession().flush();
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

    public static void insertDocPeriods(final Integer userId, final PeriodsForInsertData periodsForInsertData, final int docId) throws DaoScriptException {

        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                if (userId != null) {
                    passUserIdToAuditTrigger(userId);
                }
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

    public static void setOrderStatusesToArrived(final Integer userId, final long donutDocPeriodId) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                if (userId != null) {
                    passUserIdToAuditTrigger(userId);
                }
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                Set<Order> orders = donutDocPeriod.getOrders();
                for (Order order : orders) {
                    order.setOrderStatus(OrderStatuses.ARRIVED);
                }
            }
        });
    }

    public static void updateDonutWithRequests(final Integer userId, final DonutData donutUpdateData) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                if (userId != null) {
                    passUserIdToAuditTrigger(userId);
                }
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, (long) donutUpdateData.donutDocPeriodId);
                donutDocPeriod.setDriverPhoneNumber(donutUpdateData.driverPhoneNumber);
                donutDocPeriod.setDriver(donutUpdateData.driver);
                donutDocPeriod.setCommentForDonut(donutUpdateData.commentForDonut);
                donutDocPeriod.setLicensePlate(donutUpdateData.licensePlate);
                donutDocPeriod.setPalletsQty((short) donutUpdateData.palletsQty);


                OrderDao orderDao = new OrderDaoImpl();
                WarehouseDao warehouseDao = new WarehouseDaoImpl();
                Set<OrderData> ordersUpdateData = donutUpdateData.orders;
                for (OrderData orderUpdateData : ordersUpdateData) {
                    if (orderUpdateData.orderId == null) {
                        // insert
                        Order order = new Order();
                        order.setOrderStatus(OrderStatuses.valueOf(orderUpdateData.orderStatusId));
                        order.setOrderNumber(orderUpdateData.orderNumber);
                        order.setFinalDestinationWarehouse(warehouseDao.findById(Warehouse.class, orderUpdateData.finalDestinationWarehouseId));
                        order.setBoxQty((short) orderUpdateData.boxQty);
                        order.setDonutDocPeriod(donutDocPeriod);
                        order.setCommentForStatus(orderUpdateData.commentForStatus);
                        order.setInvoiceNumber(orderUpdateData.invoiceNumber);
                        order.setGoodsCost(orderUpdateData.goodsCost);
                        order.setOrderPalletsQty((short) orderUpdateData.orderPalletsQty);
                        orderDao.save(order);
                    } else {
                        // update
                        Order order = orderDao.findById(Order.class, orderUpdateData.orderId);
                        order.setOrderStatus(OrderStatuses.valueOf(orderUpdateData.orderStatusId));
                        order.setOrderNumber(orderUpdateData.orderNumber);
                        order.setFinalDestinationWarehouse(warehouseDao.findById(Warehouse.class, orderUpdateData.finalDestinationWarehouseId));
                        order.setBoxQty((short) orderUpdateData.boxQty);
                        order.setCommentForStatus(orderUpdateData.commentForStatus);
                        order.setInvoiceNumber(orderUpdateData.invoiceNumber);
                        order.setGoodsCost(orderUpdateData.goodsCost);
                        order.setOrderPalletsQty((short) orderUpdateData.orderPalletsQty);
                        orderDao.update(order);
                    }
                }
                donutDocPeriodDao.update(donutDocPeriod);
                // delete
                if (donutUpdateData.ordersIdForDelete != null) {
                    for (Integer orderIdForDelete : donutUpdateData.ordersIdForDelete) {
                        Order order = orderDao.findById(Order.class, orderIdForDelete);
                        order.setOrderId(orderIdForDelete);
                        orderDao.delete(order);
                    }
                }

            }

        });
    }

    public static void updateDonutPeriods(final Integer userId, final long donutDocPeriodId, final boolean isBegin) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                if (userId != null) {
                    passUserIdToAuditTrigger(userId);
                }
                update(donutDocPeriodId, isBegin);
            }
        });
    }

    public static void updateDonutPeriodsIfCreated(final Integer userId, final long donutDocPeriodId, final boolean isBegin) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                if (userId != null) {
                    passUserIdToAuditTrigger(userId);
                }
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                Set<Order> orders = donutDocPeriod.getOrders();
                for (Order order : orders) {
                    if (order.getOrderStatus() != OrderStatuses.CREATED)
                        throw new DAOException("У одной из заявок статус отличается от " + RusNames.getOrderStatusesConverter().get(OrderStatuses.CREATED));
                }
                update(donutDocPeriodId, isBegin);
            }
        });
    }

    private static void update(final long donutDocPeriodId, final boolean isBegin) throws DAOException {
        DocPeriodDao docPeriodDao = new DocPeriodDaoImpl();
        DocPeriod docPeriod = docPeriodDao.findById(DocPeriod.class, donutDocPeriodId);
        Period period = docPeriod.getPeriod();
        Calendar cal = Calendar.getInstance();
        cal.setTime(isBegin ? period.getPeriodBegin() : period.getPeriodEnd());
        cal.add(Calendar.MINUTE, isBegin ? 30 : -30);
        if (isBegin) {
            period.setPeriodBegin(cal.getTime());
        } else {
            period.setPeriodEnd(cal.getTime());
        }
        docPeriod.setPeriod(period);
        docPeriodDao.update(docPeriod);
    }

    public static void deleteDonutWithRequests(final Integer userId, final long donutDocPeriodId) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                if (userId != null) {
                    passUserIdToAuditTrigger(userId);
                }
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

    public static void deleteDonutWithRequestsIfCreated(final Integer userId, final long donutDocPeriodId) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                if (userId != null) {
                    passUserIdToAuditTrigger(userId);
                }
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                OrderDao orderDao = new OrderDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                Set<Order> orders = donutDocPeriod.getOrders();
                for (Order order : orders) {
                    if (order.getOrderStatus() != OrderStatuses.CREATED)
                        throw new DAOException("У одной из заявок статус отличается от " + RusNames.getOrderStatusesConverter().get(OrderStatuses.CREATED));
                }
                for (Order order : orders) {
                    orderDao.delete(order);
                }
                donutDocPeriodDao.delete(donutDocPeriod);
            }
        });

    }

    public static void insertDonut(final Integer userId, final DonutData donutInsertData, final DocDateSelectorData docDateSelectorData, final SupplierUser supplierUser) throws DaoScriptException {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                if (userId != null) {
                    passUserIdToAuditTrigger(userId);
                }
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
                for (OrderData dtoOrder : donutInsertData.orders) {
                    Order order = new Order();
                    order.setBoxQty((short) dtoOrder.boxQty);
                    order.setCommentForStatus(dtoOrder.commentForStatus);
                    order.setFinalDestinationWarehouse(warehouseDao.findById(Warehouse.class, dtoOrder.finalDestinationWarehouseId));
                    order.setOrderNumber(dtoOrder.orderNumber);
                    order.setOrderStatus(OrderStatuses.valueOf(dtoOrder.orderStatusId));
                    order.setDonutDocPeriod(donutDocPeriod);
                    order.setInvoiceNumber(dtoOrder.invoiceNumber);
                    order.setGoodsCost(dtoOrder.goodsCost);
                    order.setOrderPalletsQty((short) dtoOrder.orderPalletsQty);
                    orderDao.save(order);
                }
            }
        });
    }

}
