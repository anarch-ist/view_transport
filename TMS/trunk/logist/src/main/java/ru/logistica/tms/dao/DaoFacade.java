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
import ru.logistica.tms.dto.AuthResult;
import ru.logistica.tms.dto.DocDateSelectorData;
import ru.logistica.tms.dto.Donut;
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

    public static void updateDonutPeriod(final long donutDocPeriodId, final Date periodBegin, final Date periodEnd) {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                DonutDocPeriodDao donutDocPeriodDao = new DonutDocPeriodDaoImpl();
                DonutDocPeriod donutDocPeriod = donutDocPeriodDao.findById(DonutDocPeriod.class, donutDocPeriodId);
                donutDocPeriod.setPeriod(new Period(periodBegin, periodEnd));
                donutDocPeriodDao.update(donutDocPeriod);
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

    public static void insertDonut(final Donut donut, final DocDateSelectorData docDateSelectorData, final Supplier usersSupplier) {
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
                for (Donut.Order dtoOrder: donut.orders) {
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

    public static Warehouse getWarehouseWithDocsForUser(final Integer userId) {
        final Warehouse[] result = new Warehouse[1];
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                WarehouseUserDao warehouseUserDao = new WarehouseUserDaoImpl();
                WarehouseUser warehouseUser = warehouseUserDao.findById(WarehouseUser.class, userId);
                result[0] = warehouseUser.getWarehouse();
            }
        });
        return result[0];
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
                DocPeriodDao docPeriodDao = new DocPeriodImpl();
                List<DocPeriod> allPeriodsBetweenTimeStampsForDoc =
                        docPeriodDao.findAllPeriodsBetweenTimeStampsForDoc(docId, timeStampBegin, timeStampEnd);
                result[0] = allPeriodsBetweenTimeStampsForDoc;
            }
        });
        return (List<DocPeriod>) result[0];
    }


    public static Warehouse getWarehouseById(final Integer warehouseId) {
        final Warehouse[] result = new Warehouse[1];
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DAOException {
                WarehouseDao warehouseDao = new WarehouseDaoImpl();
                Warehouse warehouse = warehouseDao.findById(Warehouse.class, warehouseId);
                warehouse.getRusTimeZoneAbbr();
                result[0] = warehouse;
            }
        });
        return result[0];
    }

}
