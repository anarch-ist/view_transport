package ru.logistica.tms.dao.audit.orderAuditDao;

import ru.logistica.tms.dao.DAOException;
import ru.logistica.tms.dao.GenericDaoImpl;

public class OrdersAuditDaoImpl extends GenericDaoImpl<OrdersAudit, Long> implements OrdersAuditDao{
    @Override
    public void update(OrdersAudit entity) throws DAOException {
        throw new UnsupportedOperationException("Not supported operation");
    }

    @Override
    public void delete(OrdersAudit entity) throws DAOException {
        throw new UnsupportedOperationException("Not supported operation");
    }

    @Override
    public Long save(OrdersAudit entity) throws DAOException {
        throw new UnsupportedOperationException("Not supported operation");
    }

    @Override
    public void persist(OrdersAudit entity) throws DAOException {
        throw new UnsupportedOperationException("Not supported operation");
    }
}
