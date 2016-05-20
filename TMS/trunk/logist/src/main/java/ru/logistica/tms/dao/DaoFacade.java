package ru.logistica.tms.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.dao.cache.AppContextCache;
import ru.logistica.tms.dao.docDao.Doc;
import ru.logistica.tms.dao.docDao.DocDao;
import ru.logistica.tms.dao.docDao.DocDaoImpl;
import ru.logistica.tms.dao.userDao.*;
import ru.logistica.tms.dao.warehouseDao.RusTimeZoneAbbr;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dao.warehouseDao.WarehouseDao;
import ru.logistica.tms.dao.warehouseDao.WarehouseDaoImpl;
import ru.logistica.tms.dto.AuthResult;
import ru.logistica.tms.util.CriptUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

//    public static Map<Integer, String> getAllPointsAsIdAndNameMap() {
//        // Map<Integer, String> result =
//        doInTransaction(new DaoScript() {
//            @Override
//            public void execute() throws DaoException {
//                GenericDao<Warehouse> warehouseGenericDao = new WarehouseDaoImpl();
//                warehouseGenericDao.getAll();
//            }
//        });
//    }

}
