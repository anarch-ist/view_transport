package ru.logistica.tms.dao2.docPeriodDao;

import org.hibernate.Query;
import ru.logistica.tms.dao2.DAOException;
import ru.logistica.tms.dao2.GenericDao;
import ru.logistica.tms.dao2.GenericDaoImpl;

import java.util.Date;
import java.util.List;

public class DocPeriodImpl extends GenericDaoImpl<DocPeriod, Long> implements DocPeriodDao {

    @Override
    public List<DocPeriod> findAllPeriodsBetweenTimeStamps(Date utcTimestampBegin, Date utcTimestampEnd) throws DAOException {
        String queryString = "FROM DocPeriod WHERE period.periodBegin >= :tsBegin AND period.periodEnd <= :tsEnd ORDER BY period.periodBegin";
        Query query = getSession().createQuery(queryString);
        query.setTimestamp("tsBegin", utcTimestampBegin);
        query.setTimestamp("tsEnd", utcTimestampEnd);
        return query.list();
    }
}
