package ru.logistica.tms.dao.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logistica.tms.dao.constantsDao.*;

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

    public static boolean checkUser(final String login, final String passMd5) {

        doInTransaction(new DaoScript() {
            @Override
            public void execute() throws DaoException {

            }
        });


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
                Set<DonutStatus> donutStatuses = constantsDao.getDonutStatuses();
                ConstantCollections.setDonutStatuses(donutStatuses);
            }
        });
    }
}
