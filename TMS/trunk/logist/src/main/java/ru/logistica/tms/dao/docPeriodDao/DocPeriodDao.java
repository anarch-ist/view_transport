package ru.logistica.tms.dao.docPeriodDao;

import ru.logistica.tms.dao.DAOException;
import ru.logistica.tms.dao.GenericDao;

import java.util.Date;
import java.util.List;

public interface DocPeriodDao extends GenericDao<DocPeriod, Long> {
    /**
     *
     * @param utcTimestampBegin begin point in time
     * @param utcTimestampEnd end point in time
     * @return ASC sorted list of Periods between utcTimestampBegin and utcTimestampEnd.
     * @throws DAOException
     */
    List<DocPeriod> findAllPeriodsBetweenTimeStamps(Date utcTimestampBegin, Date utcTimestampEnd) throws DAOException;
}