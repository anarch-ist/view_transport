package ru.logistica.tms.dao.warehouseDao;

import ru.logistica.tms.dao.DAOException;
import ru.logistica.tms.dao.GenericDao;

public interface WarehouseDao extends GenericDao<Warehouse, Integer> {
    Double findOffsetByAbbreviation(RusTimeZoneAbbr rusTimeZoneAbbr) throws DAOException;
}
