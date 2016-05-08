package ru.logistica.tms.dao.warehouseDao;

import ru.logistica.tms.dao.DaoException;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

public class PeriodDaoImpl implements PeriodDao {

    @Override
    public Set<Period> getPeriodsInInterval(Timestamp begin, Timestamp end) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Period getById(Integer id) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Collection<Period> getAll() throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void deleteById(Integer id) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Integer saveOrUpdate(Period entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Integer save(Period entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void update(Period entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
