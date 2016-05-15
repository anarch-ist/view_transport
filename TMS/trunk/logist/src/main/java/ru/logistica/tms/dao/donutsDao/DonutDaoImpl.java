package ru.logistica.tms.dao.donutsDao;

import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.suppliersDao.Supplier;
import ru.logistica.tms.dao.suppliersDao.SupplierDao;
import ru.logistica.tms.dao.suppliersDao.SupplierDaoImpl;
import ru.logistica.tms.dao.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;

public class DonutDaoImpl implements DonutDao {
    private SupplierDao supplierDao = new SupplierDaoImpl();

    @Override
    public Donut getById(final Integer id) throws DaoException {
        final Donut[] result = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from donuts WHERE donutid = ? ";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();
                    result[0] = new Donut();
                    result[0].setDonutId(resultSet.getInt("donutID"));
                    result[0].setCreationDate(resultSet.getDate("creationDate"));
                    result[0].setCreationDate(resultSet.getDate("creationDate"));
                    result[0].setComment(resultSet.getString("comment"));
                    result[0].setDriver(resultSet.getString("driver"));
                    result[0].setDriverPhoneNumber(resultSet.getString("driverPhoneNumber"));
                    result[0].setLicensePlate(resultSet.getString("licensePlate"));
                    result[0].setPalletsQty(resultSet.getInt("palletsQty"));
                    result[0].setSupplier(supplierDao.getById(resultSet.getInt("supplierID")));
                }
            }
        });
        return result[0];
    }

    @Override
    public Collection<Donut> getAll() throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void deleteById(Integer id) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Integer saveOrUpdate(Donut donut) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Integer save(final Donut donut) throws DaoException {
        final Integer[] result = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO donuts (creationdate, comment, driver, driverphonenumber, licenseplate, palletsqty, supplierid) VALUES (?,?,?,?,?,?,?)";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setDate(1, donut.getCreationDate());
                    statement.setString(2, donut.getComment());
                    statement.setString(3, donut.getDriver());
                    statement.setString(4, donut.getDriverPhoneNumber());
                    statement.setString(5, donut.getLicensePlate());
                    statement.setInt(6, donut.getPalletsQty());
                    statement.setInt(7, donut.getSupplier().getSupplierID());

                    statement.execute();
                    ResultSet resultSet = statement.getGeneratedKeys();
                    if(resultSet.next()) {
                        result[0] = resultSet.getInt(1);
                        donut.setDonutId(result[0]);
                    }
                }
            }
        });
        return result[0];
    }

    @Override
    public void update(final Donut donut) throws DaoException {

//        Utils.runWithExceptionRedirect(new Utils.Exec() {
//            @Override
//            public void execute() throws Exception {
//                String sql = "UPDATE donuts SET creationDate = ?, comment = ?, driver = ?, driverPhoneNumber = ?, " +
//                        "licensePlate = ?, palletsQty = ?, supplierID = ? WHERE donuts.donutid = ?";
//                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//                    statement.setDate(1, donut.getCreationDate());
//                    statement.setString(2, donut.getComment());
//                    statement.setString(3, donut.getDriver());
//                    statement.setString(4, donut.getDriverPhoneNumber());
//                    statement.setString(5, donut.getLicensePlate());
//                    statement.setInt(6, donut.getPalletsQty());
//                    statement.setInt(7, donut.getSupplier().getSupplierID());
//
//                    statement.execute();
//                    countOfUpdated[0] = statement.getUpdateCount();
//                }
//            }
//        });
    }
}
