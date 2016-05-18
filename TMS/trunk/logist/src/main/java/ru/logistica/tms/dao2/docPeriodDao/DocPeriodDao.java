package ru.logistica.tms.dao2.docPeriodDao;

import ru.logistica.tms.dao2.DAOException;
import ru.logistica.tms.dao2.GenericDao;

import java.util.Date;
import java.util.List;

public interface DocPeriodDao extends GenericDao<DocPeriod, Long> {
    List<Period> findAllPeriodsBetweenTimeStamps(Date timeStampBegin, Date timeStampEnd) throws DAOException;
}
