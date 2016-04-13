package ru.logistica.tms.dao.constantsDao;

import ru.logistica.tms.dao.usersDao.DonutStatus;
import ru.logistica.tms.dao.usersDao.Permission;
import ru.logistica.tms.dao.usersDao.TimeDiff;
import ru.logistica.tms.dao.usersDao.UserRole;

import java.sql.SQLException;
import java.util.Set;

public class ConstantCollections {
    private static Set<UserRole> userRoles;
    private static Set<Permission> permissions;
    private static Set<TimeDiff> timeDiffs;
    private static Set<DonutStatus> donutStatuses;


    public static Set<UserRole> getUserRoles() throws SQLException {
        return userRoles;
    }

    public static void setUserRoles(Set<UserRole> userRoles) {
        ConstantCollections.userRoles = userRoles;
    }

    public static Set<Permission> getPermissions() throws SQLException {
        return permissions;
    }

    public static void setPermissions(Set<Permission> permissions) {
        ConstantCollections.permissions = permissions;
    }

    public static Set<TimeDiff> getTimeDiffs() throws SQLException {
        return timeDiffs;
    }

    public static void setTimeDiffs(Set<TimeDiff> timeDiffs) {
        ConstantCollections.timeDiffs = timeDiffs;
    }

    public static Set<DonutStatus> getDonutStatuses() throws SQLException {
        return donutStatuses;
    }

    public static void setDonutStatuses(Set<DonutStatus> donutStatuses) {
        ConstantCollections.donutStatuses = donutStatuses;
    }
}
