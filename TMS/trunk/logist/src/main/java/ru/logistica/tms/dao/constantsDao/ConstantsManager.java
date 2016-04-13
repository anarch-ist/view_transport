package ru.logistica.tms.dao.constantsDao;


import ru.logistica.tms.dao.JdbcUtil;
import ru.logistica.tms.dao.usersDao.DonutStatus;
import ru.logistica.tms.dao.usersDao.Permission;
import ru.logistica.tms.dao.usersDao.TimeDiff;
import ru.logistica.tms.dao.usersDao.UserRole;

import java.sql.SQLException;
import java.util.Set;

public class ConstantsManager {
    private ConstantsDao constantsDao = new ConstantsDaoImpl();

    public void getAllConstants() {
        try {
            Set<UserRole> userRoles = constantsDao.getUserRoles();
            ConstantCollections.setUserRoles(userRoles);
            Set<Permission> permissions = constantsDao.getPermissions();
            ConstantCollections.setPermissions(permissions);
            Set<TimeDiff> timeDiffs = constantsDao.getTimeDiffs();
            ConstantCollections.setTimeDiffs(timeDiffs);
            Set<DonutStatus> donutStatuses = constantsDao.getDonutStatuses();
            ConstantCollections.setDonutStatuses(donutStatuses);
            JdbcUtil.getConnection().commit();
        } catch (SQLException e) {
            try {
                JdbcUtil.getConnection().rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
