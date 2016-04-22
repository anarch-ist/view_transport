package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.GenericDao;
import ru.logistica.tms.dao.utils.JdbcUtil;
import ru.logistica.tms.dao.suppliersDao.Supplier;
import ru.logistica.tms.dao.suppliersDao.SupplierDaoImpl;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SupplierUserDaoImpl implements GenericUserDao<SupplierUser> {

    private GenericUserDao<AbstractUser> abstractUserDao = new AbstractUserDaoImpl();
    private GenericDao<Supplier> supplierDao = new SupplierDaoImpl();

    @Override
    public SupplierUser getByLogin(final String login) throws DaoException {
        AbstractUser abstractUser = abstractUserDao.getByLogin(login);
        if(abstractUser == null){
            return null;
        }else {
            Integer userId = abstractUserDao.getByLogin(login).getUserId();
            return getById(userId);
        }
    }

    @Override
    public void deleteUserByLogin(final String login) throws DaoException {
        abstractUserDao.deleteUserByLogin(login);
    }

    @Override
    public SupplierUser getById(final Integer id) throws DaoException {
        final SupplierUser[] supplierUser = {null};
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
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
                        supplierUser[0] = new SupplierUser();
                        supplierUser[0].setUserId(resultSet.getInt("userID"));
                        Integer supplierId = resultSet.getInt("supplierID");
                        Supplier supplier = supplierDao.getById(supplierId);
                        supplierUser[0].setLogin(resultSet.getString("userLogin"));
                        supplierUser[0].setSalt(resultSet.getString("salt"));
                        supplierUser[0].setPassAndSalt(resultSet.getString("passAndSalt"));
                        String userRoleId = resultSet.getString("userRoleID");
                        supplierUser[0].setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
                        supplierUser[0].setUserName(resultSet.getString("userName"));
                        supplierUser[0].setPhoneNumber(resultSet.getString("phoneNumber"));
                        supplierUser[0].setEmail(resultSet.getString("email"));
                        supplierUser[0].setPosition(resultSet.getString("position"));
                        supplierUser[0].setSupplier(supplier);
                    }
                }
            }
        });
        return supplierUser[0];
    }

    @Override
    public Collection<SupplierUser> getAll() throws DaoException {
        final Set<SupplierUser> result = new HashSet<>();
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
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
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        SupplierUser supplierUser = new SupplierUser();
                        supplierUser.setUserId(resultSet.getInt("userID"));
                        Integer supplierId = resultSet.getInt("supplierID");
                        Supplier supplier = supplierDao.getById(supplierId);
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
            }
        });
        return result;
    }

    @Override
    public void deleteById(Integer id) throws DaoException {
        abstractUserDao.deleteById(id);
    }

    @Override
    public Integer saveOrUpdate(final SupplierUser supplierUser) throws DaoException {
        final Integer userId = abstractUserDao.saveOrUpdate(supplierUser);
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                // if was inserted
                if (userId != null) {
                    String sql = "INSERT INTO suppliers_users VALUES (?, ?)";
                    try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
                        statement.setInt(1, userId);
                        statement.setInt(2, (Integer) supplierUser.getSupplier().getSupplierID());
                        statement.execute();
                    }
                }
            }
        });
        return userId;
    }

    @Override
    public Integer save(final SupplierUser supplierUser) throws DaoException {
        final Integer userId = abstractUserDao.save(supplierUser);
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO suppliers_users VALUES (?, ?)";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)) {
                    statement.setInt(1, userId);
                    statement.setInt(2, (Integer) supplierUser.getSupplier().getSupplierID());
                    statement.execute();
                }
            }
        });
        return userId;
    }

    @Override
    public void update(final SupplierUser supplierUser) throws DaoException {
        abstractUserDao.update(supplierUser);
    }
}
