package ru.logistica.tms.dao;

import ru.logistica.tms.dao.usersDao.DonutStatus;
import ru.logistica.tms.dao.usersDao.Permission;
import ru.logistica.tms.dao.usersDao.TimeDiff;
import ru.logistica.tms.dao.usersDao.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ConstantCollections {
    private static Set<UserRole> userRoles;
    private static Set<Permission> permissions;
    private static Set<TimeDiff> timeDiffs;
    private static Set<DonutStatus> donutStatuses;

    public static Set<UserRole> getUserRoles() throws SQLException {
        String sql = "SELECT * from user_roles";
        Set<UserRole> result = new HashSet<>();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                UserRole userRole = new UserRole();
                userRole.setUserRoleId(resultSet.getString("userRoleID"));
                userRole.setUserRoleRusName(resultSet.getString("userRoleRusName"));
                result.add(userRole);
            }
        }
        return result;
    }

    public static void setUserRoles(Set<UserRole> userRoles) {
        ConstantCollections.userRoles = userRoles;
    }

    public static Set<Permission> getPermissions() throws SQLException {
        String sql = "SELECT * from permissions";
        Set<Permission> result = new HashSet<>();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Permission permission = new Permission();
                permission.setPermissionId(resultSet.getString("permissionID"));
                result.add(permission);
            }
        }
        return result;
    }

    public static void setPermissions(Set<Permission> permissions) {
        ConstantCollections.permissions = permissions;
    }

    public static Set<TimeDiff> getTimeDiffs() throws SQLException {
        String sql = "SELECT * from time_diffs";
        Set<TimeDiff> result = new HashSet<>();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                TimeDiff timeDiff = new TimeDiff();
                timeDiff.setTimeDiffId(resultSet.getShort("timeDiffID"));
                result.add(timeDiff);
            }
        }
        return result;
    }

    public static void setTimeDiffs(Set<TimeDiff> timeDiffs) {
        ConstantCollections.timeDiffs = timeDiffs;
    }

    public static Set<DonutStatus> getDonutStatuses() throws SQLException {
        String sql = "SELECT * from donut_statuses";
        Set<DonutStatus> result = new HashSet<>();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                DonutStatus donutStatus = new DonutStatus();
                donutStatus.setDonutStatusId(resultSet.getString("donutStatusID"));
                donutStatus.setDonutStatusRusName(resultSet.getString("donutStatusRusName"));
                result.add(donutStatus);
            }
        }
        return result;
    }

    public static void setDonutStatuses(Set<DonutStatus> donutStatuses) {
        ConstantCollections.donutStatuses = donutStatuses;
    }
}
