package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.GenericDao;
import ru.logistica.tms.dao.suppliersDao.Supplier;
import ru.logistica.tms.dao.suppliersDao.SupplierDaoImpl;
import ru.logistica.tms.dao.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SupplierUserDaoImpl implements GenericUserDao<SupplierUser> {

    private GenericUserDao<User> userDao = new UserDaoImpl();
    private GenericDao<Supplier> supplierDao = new SupplierDaoImpl();

    @Override
    public SupplierUser getByLogin(final String login) throws DaoException {
        User user = userDao.getByLogin(login);
        if(user == null){
            return null;
        }else {
            Integer userId = userDao.getByLogin(login).getUserId();
            return getById(userId);
        }
    }

    @Override
    public void deleteUserByLogin(final String login) throws DaoException {
        userDao.deleteUserByLogin(login);
    }

    @Override
    public SupplierUser getById(final Integer id) throws DaoException {
        final SupplierUser[] supplierUser = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT suppliers_users.userID, " +
                        "suppliers.supplierID, " +
                        "users.userLogin, " +
                        "users.salt, " +
                        "users.passAndSalt, " +
                        "users.userRoleID, " +
                        "users.userName, " +
                        "users.phoneNumber, " +
                        "users.email, " +
                        "users.position, " +
                        "suppliers.INN " +
                        "FROM suppliers_users " +
                        "INNER JOIN users ON (users.userID = suppliers_users.userID) " +
                        "INNER JOIN suppliers ON (suppliers.supplierID = suppliers_users.supplierID)" +
                        "WHERE suppliers_users.userID = ?;";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
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
                        supplierUser[0].setUserRole(User.UserRole.valueOf(resultSet.getString("userRoleID")));
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
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT suppliers_users.userID, " +
                        "suppliers.supplierID, " +
                        "users.userLogin, " +
                        "users.salt, " +
                        "users.passAndSalt, " +
                        "users.userRoleID, " +
                        "users.userName, " +
                        "users.phoneNumber, " +
                        "users.email, " +
                        "users.position, " +
                        "suppliers.INN " +
                        "FROM suppliers_users " +
                        "INNER JOIN users ON (users.userID = suppliers_users.userID)\n" +
                        "INNER JOIN suppliers ON (suppliers.supplierID = suppliers_users.supplierID);";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        SupplierUser supplierUser = new SupplierUser();
                        supplierUser.setUserId(resultSet.getInt("userID"));
                        Integer supplierId = resultSet.getInt("supplierID");
                        Supplier supplier = supplierDao.getById(supplierId);
                        supplierUser.setLogin(resultSet.getString("userLogin"));
                        supplierUser.setSalt(resultSet.getString("salt"));
                        supplierUser.setPassAndSalt(resultSet.getString("passAndSalt"));
                        supplierUser.setUserRole(User.UserRole.valueOf(resultSet.getString("userRoleID")));
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
        userDao.deleteById(id);
    }

    @Override
    public Integer saveOrUpdate(final SupplierUser supplierUser) throws DaoException {
        final Integer userId = userDao.saveOrUpdate(supplierUser);
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                // if was inserted
                if (userId != null) {
                    String sql = "INSERT INTO suppliers_users VALUES (?, ?)";
                    try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                        statement.setInt(1, userId);
                        statement.setInt(2, supplierUser.getSupplier().getSupplierID());
                        statement.execute();
                    }
                }
            }
        });
        return userId;
    }

    @Override
    public Integer save(final SupplierUser supplierUser) throws DaoException {
        final Integer userId = userDao.save(supplierUser);
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO suppliers_users VALUES (?, ?)";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)) {
                    statement.setInt(1, userId);
                    statement.setInt(2, supplierUser.getSupplier().getSupplierID());
                    statement.execute();
                }
            }
        });
        return userId;
    }

    @Override
    public void update(final SupplierUser supplierUser) throws DaoException {
        userDao.update(supplierUser);
    }
}
