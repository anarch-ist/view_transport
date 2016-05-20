package ru.logistica.tms.dao.warehouseDao;

import org.hibernate.SQLQuery;
import ru.logistica.tms.dao.DAOException;
import ru.logistica.tms.dao.GenericDaoImpl;

public class WarehouseDaoImpl extends GenericDaoImpl<Warehouse, Integer> implements WarehouseDao {
    @Override
    public Double findOffsetByAbbreviation(RusTimeZoneAbbr rusTimeZoneAbbr) throws DAOException {
        String sql = "SELECT DISTINCT EXTRACT(HOUR FROM utc_offset) FROM pg_timezone_names WHERE abbrev = :abbreviation";
        SQLQuery sqlQuery = getSession().createSQLQuery(sql);
        sqlQuery.setString("abbreviation", rusTimeZoneAbbr.name());
        return (Double)sqlQuery.uniqueResult();
    }
}
