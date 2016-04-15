package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.JdbcUtil;
import ru.logistica.tms.dao.Supplier;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SupplierUserDaoImpl implements GenericUserDao<SupplierUser> {

    private GenericUserDao abstractUserDao = new AbstractUserDaoImpl();

    @Override
    public Set<SupplierUser> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM suppliers_users";
        Set<SupplierUser> result = new HashSet<>();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                SupplierUser supplierUser = new SupplierUser();
                supplierUser.setUserId(resultSet.getInt("userID"));
                Integer supplierId = resultSet.getInt("supplierID");
                Supplier supplier = new Supplier().getSupplierById(supplierId);
                supplierUser.setSupplier(supplier);
                result.add(supplierUser);
            }
        }
        return result;
    }

    @Override
    public SupplierUser getUserById(Integer id) throws SQLException {
        SupplierUser supplierUser = new SupplierUser();
        String sql = "SELECT * from suppliers_users WHERE userID = ?";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            supplierUser.setUserId(resultSet.getInt("userID"));
            Integer supplierId = resultSet.getInt("supplierID");
            Supplier supplier = new Supplier().getSupplierById(supplierId);
            supplierUser.setSupplier(supplier);
        }
        return supplierUser;
    }

    @Override
    public Integer saveOrUpdateUser(SupplierUser supplierUser) throws SQLException {
        Integer userId = abstractUserDao.saveOrUpdateUser(supplierUser);
        String sql = "INSERT INTO suppliers_users VALUES (?, ?)";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, userId);
            statement.setInt(2, (Integer) supplierUser.getSupplier().getSupplierID());
            statement.execute();
        }
        return userId;
    }

    @Override
    public SupplierUser getByLogin(String login) throws SQLException {
        SupplierUser supplierUser = new SupplierUser();
        Integer userId = abstractUserDao.getByLogin(login).getUserId();
        String sql = "SELECT * from suppliers_users WHERE userID = ?";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            supplierUser.setUserId(resultSet.getInt("userID"));
            Integer supplierId = resultSet.getInt("supplierID");
            Supplier supplier = new Supplier().getSupplierById(supplierId);
            supplierUser.setSupplier(supplier);
        }
        return supplierUser;
    }

    @Override
    public Integer deleteUserByLogin(String login) throws SQLException {
        Integer result = abstractUserDao.deleteUserByLogin(login);
        String sql = "DELETE FROM suppliers_users WHERE userID = ?";

        try (PreparedStatement preparedStatement = JdbcUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, result);
            preparedStatement.execute();
        }
        return result;
    }
}
