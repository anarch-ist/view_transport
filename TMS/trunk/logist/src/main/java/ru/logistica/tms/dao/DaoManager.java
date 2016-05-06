package ru.logistica.tms.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.dao.constantsDao.*;
import ru.logistica.tms.dao.usersDao.GenericUserDao;
import ru.logistica.tms.dao.usersDao.User;
import ru.logistica.tms.dao.usersDao.UserDaoImpl;
import ru.logistica.tms.dao.utils.DBUtils;
import ru.logistica.tms.dto.AuthResult;
import ru.logistica.tms.util.CriptUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Set;

public class DaoManager {
    private static final Logger logger = LogManager.getLogger();
    private static DataSource dataSource;

    public static void setDataSource(DataSource dataSource) {
        DaoManager.dataSource = dataSource;
    }

    private static void doInTransaction(DaoScript daoScript) {
        try {
            ConnectionManager.setConnection(dataSource.getConnection()); // get connection from the pool
            daoScript.execute();
            ConnectionManager.getConnection().commit();
        } catch (DaoException | SQLException e) {
            logger.error(e.getMessage());
            DBUtils.rollbackQuietly(ConnectionManager.getConnection());
        } finally {
            DBUtils.closeConnectionQuietly(ConnectionManager.getConnection()); // return connection back to the pool
        }
    }
    private interface DaoScript {
        void execute() throws DaoException;
    }

    public static void loadAllConstantsInMemory() {
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DaoException {
                ConstantsDao constantsDao = new ConstantsDaoImpl();
                Set<UserRole> userRoles = constantsDao.getUserRoles();
                ConstantCollections.setUserRoles(userRoles);
                Set<Permission> permissions = constantsDao.getPermissions();
                ConstantCollections.setPermissions(permissions);
                Set<TimeDiff> timeDiffs = constantsDao.getTimeDiffs();
                ConstantCollections.setTimeDiffs(timeDiffs);
                Set<WantStatus> wantStatuses = constantsDao.getWantStatuses();
                ConstantCollections.setWantStatuses(wantStatuses);
            }
        });
    }

    public static AuthResult checkUser(final String login, final String passMd5) {
        final AuthResult authResult = new AuthResult();
        // md5(md5(pass)+salt)
        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DaoException {
                GenericUserDao<User> userDao = new UserDaoImpl();
                User user = userDao.getByLogin(login);
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

}