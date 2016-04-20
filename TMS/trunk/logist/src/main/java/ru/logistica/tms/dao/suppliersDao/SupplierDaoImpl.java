package ru.logistica.tms.dao.suppliersDao;


import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.GenericDao;
import ru.logistica.tms.dao.utils.JdbcUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;

public class SupplierDaoImpl implements GenericDao<Supplier> {

    @Override
    public Supplier getById(final Integer id) throws DaoException {
        final Supplier supplier = new Supplier();
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from suppliers WHERE supplierID = ?";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();
                    supplier.setSupplierID(resultSet.getInt("supplierID"));
                    supplier.setInn(resultSet.getString("INN"));
                    supplier.setClientName(resultSet.getString("clientName"));
                    supplier.setKpp(resultSet.getString("KPP"));
                    supplier.setCorAccount(resultSet.getString("corAccount"));
                    supplier.setCurAccount(resultSet.getString("curAccount"));
                    supplier.setBik(resultSet.getString("BIK"));
                    supplier.setBankName(resultSet.getString("bankName"));
                    supplier.setContractNumber(resultSet.getString("contractNumber"));
                    supplier.setDateOfSigning(resultSet.getDate("dateOfSigning"));
                    supplier.setStartContractDate(resultSet.getDate("startContractDate"));
                    supplier.setEndContractDate(resultSet.getDate("endContractDate"));
                }
            }
        });
        return supplier;
    }

    @Override
    public Integer saveOrUpdate(final Supplier supplier) throws DaoException {
        final Integer[] result = {null};
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO suppliers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (supplierID) DO UPDATE SET supplierID = EXCLUDED.supplierID, " +
                        "INN = EXCLUDED.INN, clientName = EXCLUDED.clientName, KPP = EXCLUDED.KPP, corAccount = EXCLUDED.corAccount, curAccount = EXCLUDED.curAccount, " +
                        "BIK = EXCLUDED.BIK, bankName = EXCLUDED.bankName, contractNumber = EXCLUDED.contractNumber, dateOfSigning = EXCLUDED.dateOfSigning, " +
                        "startContractDate = EXCLUDED.startContractDate, endContractDate = EXCLUDED.endContractDate";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setInt(1, (int)supplier.getSupplierID());
                    statement.setString(2, supplier.getInn());
                    statement.setString(3, supplier.getClientName());
                    statement.setString(4, supplier.getKpp());
                    statement.setString(5, supplier.getCorAccount());
                    statement.setString(6, supplier.getCurAccount());
                    statement.setString(7, supplier.getBik());
                    statement.setString(8, supplier.getBankName());
                    statement.setString(9, supplier.getContractNumber());
                    statement.setDate(10, new java.sql.Date(supplier.getDateOfSigning().getTime()));
                    statement.setDate(11, new java.sql.Date(supplier.getStartContractDate().getTime()));
                    statement.setDate(12, new java.sql.Date(supplier.getEndContractDate().getTime()));
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
    public Integer save(Supplier entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void update(Supplier entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Collection<Supplier> getAll() throws DaoException {
        return null;
    }

    @Override
    public void deleteById(Integer id) throws DaoException {

    }
}
