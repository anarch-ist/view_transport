package ru.logistica.tms.dao.constantsDao;

import ru.logistica.tms.dao.usersDao.DonutStatus;
import ru.logistica.tms.dao.usersDao.Permission;
import ru.logistica.tms.dao.usersDao.TimeDiff;
import ru.logistica.tms.dao.usersDao.UserRole;

import java.sql.SQLException;
import java.util.Set;

public interface ConstantsDao {
    Set<UserRole> getUserRoles() throws SQLException;
    Set<Permission> getPermissions() throws SQLException;
    Set<TimeDiff> getTimeDiffs() throws SQLException;
    Set<DonutStatus> getDonutStatuses() throws SQLException;
}
