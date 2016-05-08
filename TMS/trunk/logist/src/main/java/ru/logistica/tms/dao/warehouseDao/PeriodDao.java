package ru.logistica.tms.dao.warehouseDao;

import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.GenericDao;

import java.sql.Timestamp;
import java.util.Set;

public interface PeriodDao extends GenericDao<Period>{
    Set<Period> getPeriodsInInterval(Timestamp begin, Timestamp end) throws DaoException;
}
