package ru.logistica.tms.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.dao.userDao.User;
import ru.logistica.tms.dao.userDao.UserDao;
import ru.logistica.tms.dao.userDao.UserDaoImpl;
import ru.logistica.tms.dto.AuthResult;
import ru.logistica.tms.util.CriptUtils;

public class DaoManager {
    private static final Logger logger = LogManager.getLogger();

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
    private interface DaoScript {
        void execute() throws DAOException;
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
