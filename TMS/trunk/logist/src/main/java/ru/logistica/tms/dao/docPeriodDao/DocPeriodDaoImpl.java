package ru.logistica.tms.dao.docPeriodDao;

import org.hibernate.Query;
import ru.logistica.tms.dao.DAOException;
import ru.logistica.tms.dao.GenericDao;
import ru.logistica.tms.dao.GenericDaoImpl;
import ru.logistica.tms.dao.docDao.Doc;

import java.util.Date;
import java.util.List;

public class DocPeriodDaoImpl extends GenericDaoImpl<DocPeriod, Long> implements DocPeriodDao {

    @Override
    public List<DocPeriod> findAllPeriodsBetweenTimeStampsForDoc(Integer docId, Date utcTimestampBegin, Date utcTimestampEnd) throws DAOException {
        String queryString = "FROM DocPeriod WHERE (period.periodBegin >= :tsBegin AND period.periodEnd <= :tsEnd AND doc.docId = :docId) ORDER BY period.periodBegin";
        Query query = getSession().createQuery(queryString);
        query.setInteger("docId", docId);
        query.setTimestamp("tsBegin", utcTimestampBegin);
        query.setTimestamp("tsEnd", utcTimestampEnd);
        return query.list();
    }
}
