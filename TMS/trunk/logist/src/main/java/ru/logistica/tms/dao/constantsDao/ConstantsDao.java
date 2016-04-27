package ru.logistica.tms.dao.constantsDao;

import ru.logistica.tms.dao.DaoException;

import java.util.Set;

public interface ConstantsDao {
    Set<UserRole> getUserRoles() throws DaoException;
    Set<Permission> getPermissions() throws DaoException;
    Set<TimeDiff> getTimeDiffs() throws DaoException;
    Set<DonutStatus> getDonutStatuses() throws DaoException;
}
