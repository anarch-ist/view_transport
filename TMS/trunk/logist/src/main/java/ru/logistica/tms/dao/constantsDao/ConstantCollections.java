package ru.logistica.tms.dao.constantsDao;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class ConstantCollections {
    private static Set<UserRole> userRoles;
    private static Set<Permission> permissions;
    private static Set<TimeDiff> timeDiffs;
    private static Set<DonutStatus> donutStatuses;


    public static Set<UserRole> getUserRoles() throws SQLException {
        return userRoles;
    }

    public static UserRole getUserRoleByUserRoleId(String userRoleId) {
        Objects.requireNonNull(userRoleId, "userRoleId must not be null");
        for (UserRole userRole : userRoles) {
            if (userRole.getUserRoleId().equals(userRoleId))
                return userRole;
        }
        throw new IllegalArgumentException("no userRoleId " + userRoleId + " in collection");
    }

    public static String getUserRoleIdByUserRole(UserRole userRole) {
        Objects.requireNonNull(userRole, "userRole must not be null");
        for (UserRole role : userRoles) {
            if(role.equals(userRole)){
                return role.getUserRoleId();
            }
        }
        throw new IllegalArgumentException("no UserRole " + userRole + " in collection");
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
