package ru.logistica.tms.dao.suppliersDao;


import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.GenericDao;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;

public class SupplierDaoImpl implements SupplierDao {

    @Override
    public Supplier getById(final Integer id) throws DaoException {
        final Supplier[] result = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from suppliers WHERE supplierID = ?";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();
                    result[0] = new Supplier();
                    result[0].setSupplierID(resultSet.getInt("supplierID"));
                    result[0].setInn(resultSet.getString("INN"));
                }
            }
        });
        return result[0];
    }

    @Override
    public Integer saveOrUpdate(final Supplier supplier) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Integer save(final Supplier supplier) throws DaoException {
        final Integer[] result = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO suppliers (inn) VALUES (?)";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setString(1, supplier.getInn());
                    statement.execute();
                    ResultSet resultSet = statement.getGeneratedKeys();
                    if(resultSet.next()) {
                        result[0] = resultSet.getInt(1);
                        supplier.setSupplierID(result[0]);
                    }
                }
            }
        });
        return result[0];
    }

    @Override
    public void update(Supplier entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Collection<Supplier> getAll() throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void deleteById(Integer id) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
