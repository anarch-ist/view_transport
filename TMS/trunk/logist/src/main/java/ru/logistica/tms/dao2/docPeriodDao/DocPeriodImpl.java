package ru.logistica.tms.dao2.docPeriodDao;

import ru.logistica.tms.dao2.DAOException;
import ru.logistica.tms.dao2.GenericDao;
import ru.logistica.tms.dao2.GenericDaoImpl;

import java.util.Date;
import java.util.List;

public class DocPeriodImpl extends GenericDaoImpl<DocPeriod, Long> implements DocPeriodDao {
    @Override
    public List<Period> findAllPeriodsBetweenTimeStamps(Date timeStampBegin, Date timeStampEnd) throws DAOException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
