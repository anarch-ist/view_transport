package ru.logistica.tms.dao.constantsDao;

import ru.logistica.tms.dao.utils.JdbcUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ConstantsDaoImpl implements ConstantsDao {


    @Override
    public Set<UserRole> getUserRoles() throws SQLException {
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

    @Override
    public Set<Permission> getPermissions() throws SQLException {
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

    @Override
    public Set<TimeDiff> getTimeDiffs() throws SQLException {
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

    @Override
    public Set<DonutStatus> getDonutStatuses() throws SQLException {
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
}
