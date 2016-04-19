package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.JdbcUtil;
import ru.logistica.tms.dao.KeyDifferenceDao;
import ru.logistica.tms.dao.Supplier;
import ru.logistica.tms.dao.SupplierDaoImpl;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SupplierUserDaoImpl implements GenericUserDao<SupplierUser> {

    private GenericUserDao abstractUserDao = new AbstractUserDaoImpl();
    private KeyDifferenceDao<Supplier> keyDifferenceDao = new SupplierDaoImpl();

    @Override
    public Set<SupplierUser> getAllUsers() throws SQLException {
        String sql = "SELECT suppliers_users.userID, " +
                "suppliers.supplierID, " +
                "abstract_users.userLogin, " +
                "abstract_users.salt, " +
                "abstract_users.passAndSalt, " +
                "abstract_users.userRoleID, " +
                "abstract_users.userName, " +
                "abstract_users.phoneNumber, " +
                "abstract_users.email, " +
                "abstract_users.position, " +
                "suppliers.INN, " +
                "suppliers.clientName, " +
                "suppliers.KPP, " +
                "suppliers.corAccount, " +
                "suppliers.curAccount, " +
                "suppliers.BIK, " +
                "suppliers.bankName, " +
                "suppliers.contractNumber, " +
                "suppliers.dateOfSigning, " +
                "suppliers.startContractDate, " +
                "suppliers.endContractDate " +
                "FROM suppliers_users " +
                "INNER JOIN abstract_users ON (abstract_users.userID = suppliers_users.userID)\n" +
                "INNER JOIN suppliers ON (suppliers.supplierID = suppliers_users.supplierID);";
        Set<SupplierUser> result = new HashSet<>();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                SupplierUser supplierUser = new SupplierUser();
                supplierUser.setUserId(resultSet.getInt("userID"));
                Integer supplierId = resultSet.getInt("supplierID");
                Supplier supplier = keyDifferenceDao.getKeyDifferenceById(supplierId);
                supplierUser.setLogin(resultSet.getString("userLogin"));
                supplierUser.setSalt(resultSet.getString("salt"));
                supplierUser.setPassAndSalt(resultSet.getString("passAndSalt"));
                String userRoleId = resultSet.getString("userRoleID");
                supplierUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
                supplierUser.setUserName(resultSet.getString("userName"));
                supplierUser.setPhoneNumber(resultSet.getString("phoneNumber"));
                supplierUser.setEmail(resultSet.getString("email"));
                supplierUser.setPosition(resultSet.getString("position"));
                supplierUser.setSupplier(supplier);
                result.add(supplierUser);
            }
        }
        return result;
    }

    @Override
    public SupplierUser getUserById(Integer id) throws SQLException {
        SupplierUser supplierUser = new SupplierUser();
        String sql = "SELECT suppliers_users.userID, " +
                "suppliers.supplierID, " +
                "abstract_users.userLogin, " +
                "abstract_users.salt, " +
                "abstract_users.passAndSalt, " +
                "abstract_users.userRoleID, " +
                "abstract_users.userName, " +
                "abstract_users.phoneNumber, " +
                "abstract_users.email, " +
                "abstract_users.position, " +
                "suppliers.INN, " +
                "suppliers.clientName, " +
                "suppliers.KPP, " +
                "suppliers.corAccount, " +
                "suppliers.curAccount, " +
                "suppliers.BIK, " +
                "suppliers.bankName, " +
                "suppliers.contractNumber, " +
                "suppliers.dateOfSigning, " +
                "suppliers.startContractDate, " +
                "suppliers.endContractDate " +
                "FROM suppliers_users " +
                "INNER JOIN abstract_users ON (abstract_users.userID = suppliers_users.userID) " +
                "INNER JOIN suppliers ON (suppliers.supplierID = suppliers_users.supplierID)" +
                "WHERE suppliers_users.userID = ?;";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                supplierUser.setUserId(resultSet.getInt("userID"));
                Integer supplierId = resultSet.getInt("supplierID");
                Supplier supplier = keyDifferenceDao.getKeyDifferenceById(supplierId);
                supplierUser.setLogin(resultSet.getString("userLogin"));
                supplierUser.setSalt(resultSet.getString("salt"));
                supplierUser.setPassAndSalt(resultSet.getString("passAndSalt"));
                String userRoleId = resultSet.getString("userRoleID");
                supplierUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
                supplierUser.setUserName(resultSet.getString("userName"));
                supplierUser.setPhoneNumber(resultSet.getString("phoneNumber"));
                supplierUser.setEmail(resultSet.getString("email"));
                supplierUser.setPosition(resultSet.getString("position"));
                supplierUser.setSupplier(supplier);
            }
        }
        return supplierUser;
    }

    @Override
    public Integer saveOrUpdateUser(SupplierUser supplierUser) throws SQLException {
        Integer userId = abstractUserDao.saveOrUpdateUser(supplierUser);
        // if was inserted
        System.out.println("generated userId = " + userId);
        if (userId != null) {
            String sql = "INSERT INTO suppliers_users VALUES (?, ?)";
            try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
                statement.setInt(1, userId);
                statement.setInt(2, (Integer) supplierUser.getSupplier().getSupplierID());
                statement.execute();
            }
        }
        return userId;
    }

    @Override
    public SupplierUser getByLogin(String login) throws SQLException {
        Integer userId = abstractUserDao.getByLogin(login).getUserId();
        SupplierUser supplierUser = getUserById(userId);
        return supplierUser;
    }

    @Override
    public void deleteUserByLogin(String login) throws SQLException {
        abstractUserDao.deleteUserByLogin(login);
    }
}
