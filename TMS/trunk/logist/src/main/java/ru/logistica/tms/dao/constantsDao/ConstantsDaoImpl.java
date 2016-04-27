package ru.logistica.tms.dao.constantsDao;

import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class ConstantsDaoImpl implements ConstantsDao {


    @Override
    public Set<UserRole> getUserRoles() throws DaoException {
        final Set<UserRole> result = new HashSet<>();
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from user_roles";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        UserRole userRole = new UserRole();
                        userRole.setUserRoleId(resultSet.getString("userRoleID"));
                        userRole.setUserRoleRusName(resultSet.getString("userRoleRusName"));
                        result.add(userRole);
                    }
                }
            }
        });
        return result;
    }

    @Override
    public Set<Permission> getPermissions() throws DaoException {
        final Set<Permission> result = new HashSet<>();
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from permissions";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        Permission permission = new Permission();
                        permission.setPermissionId(resultSet.getString("permissionID"));
                        result.add(permission);
                    }
                }
            }
        });
        return result;
    }

    @Override
    public Set<TimeDiff> getTimeDiffs() throws DaoException {
        final Set<TimeDiff> result = new HashSet<>();
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from time_diffs";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        TimeDiff timeDiff = new TimeDiff();
                        timeDiff.setTimeDiffId(resultSet.getShort("timeDiffID"));
                        result.add(timeDiff);
                    }
                }
            }
        });
        return result;
    }

    @Override
    public Set<DonutStatus> getDonutStatuses() throws DaoException {
        final Set<DonutStatus> result = new HashSet<>();
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from donut_statuses";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        DonutStatus donutStatus = new DonutStatus();
                        donutStatus.setDonutStatusId(resultSet.getString("donutStatusID"));
                        donutStatus.setDonutStatusRusName(resultSet.getString("donutStatusRusName"));
                        result.add(donutStatus);
                    }
                }
            }
        });
        return result;
    }
}
