package ru.logistica.tms.dao.warehouseDao;

import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.GenericDao;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.utils.Utils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;

public class WarehouseDaoImpl implements WarehouseDao {

    @Override
    public Warehouse getById(final Integer id) throws DaoException {
        final Warehouse warehouse = new Warehouse();
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from warehouses WHERE warehouseID = ?";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();
                    warehouse.setWarehouseId(resultSet.getInt("warehouseID"));
                    warehouse.setWarehouseName(resultSet.getString("warehouseName"));
                }
            }
        });
        return warehouse;
    }

    @Override
    public Integer saveOrUpdate(final Warehouse warehouse) throws DaoException {
        final Integer[] result = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO warehouses VALUES (?, ?) " +
                        "ON CONFLICT (warehouseID) DO UPDATE SET warehouseID = EXCLUDED.warehouseID, warehouseName = EXCLUDED.warehouseName";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setInt(1, warehouse.getWarehouseId());
                    statement.setString(2, warehouse.getWarehouseName());
                    statement.execute();

                    ResultSet resultSet = statement.getGeneratedKeys();
                    if(resultSet.next()) {
                        result[0] = resultSet.getInt(1);
                    }
                }
            }
        });
        return result[0];
    }

    @Override
    public Integer save(Warehouse entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void update(Warehouse entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Collection<Warehouse> getAll() throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void deleteById(Integer id) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
