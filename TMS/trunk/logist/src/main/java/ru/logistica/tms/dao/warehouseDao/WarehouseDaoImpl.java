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

public class WarehouseDaoImpl implements GenericDao<Warehouse> {

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
                    warehouse.setRegion(resultSet.getString("region"));
                    warehouse.setDistrict(resultSet.getString("district"));
                    warehouse.setLocality(resultSet.getString("locality"));
                    warehouse.setMailIndex(resultSet.getString("mailIndex"));
                    warehouse.setAddress(resultSet.getString("address"));
                    warehouse.setEmail(resultSet.getString("email"));
                    warehouse.setPhoneNumber(resultSet.getString("phoneNumber"));
                    warehouse.setResponsiblePersonId(resultSet.getString("responsiblePersonId"));
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
                String sql = "INSERT INTO warehouses VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (warehouseID) DO UPDATE SET warehouseID = EXCLUDED.warehouseID, " +
                        "warehouseName = EXCLUDED.warehouseName, region = EXCLUDED.region, district = EXCLUDED.district, locality = EXCLUDED.locality, mailIndex = EXCLUDED.mailIndex, " +
                        "address = EXCLUDED.address, email = EXCLUDED.email, phoneNumber = EXCLUDED.phoneNumber, responsiblePersonId = EXCLUDED.responsiblePersonId";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setInt(1, warehouse.getWarehouseId());
                    statement.setString(2, warehouse.getWarehouseName());
                    statement.setString(3, warehouse.getRegion());
                    statement.setString(4, warehouse.getDistrict());
                    statement.setString(5, warehouse.getLocality());
                    statement.setString(6, warehouse.getMailIndex());
                    statement.setString(7, warehouse.getAddress());
                    statement.setString(8, warehouse.getEmail());
                    statement.setString(9, warehouse.getPhoneNumber());
                    statement.setString(10, warehouse.getResponsiblePersonId());
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
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(Integer id) throws DaoException {
        throw new NotImplementedException();
    }
}
