package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.GenericDao;
import ru.logistica.tms.dao.utils.Utils;
import ru.logistica.tms.dao.warehouseDao.Warehouse;
import ru.logistica.tms.dao.warehouseDao.WarehouseDaoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class WarehouseUserDaoImpl implements GenericUserDao<WarehouseUser> {

    private GenericUserDao<User> userDao = new UserDaoImpl();
    private GenericDao<Warehouse> warehouseGenericDao = new WarehouseDaoImpl();

    @Override
    public WarehouseUser getByLogin(final String login) throws DaoException {
        User user = userDao.getByLogin(login);
        if(user == null){
            return null;
        }else {
            Integer userId = userDao.getByLogin(login).getUserId();
            WarehouseUser warehouseUser = getById(userId);
            return warehouseUser;
        }
    }

    @Override
    public void deleteUserByLogin(final String login) throws DaoException {
        userDao.deleteUserByLogin(login);
    }

    @Override
    public WarehouseUser getById(final Integer id) throws DaoException {
        final WarehouseUser[] warehouseUser = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT warehouse_users.userID, " +
                        "warehouse_users.warehouseID, " +
                        "users.userLogin, " +
                        "users.salt, " +
                        "users.passAndSalt, " +
                        "users.userRoleID, " +
                        "users.userName, " +
                        "users.phoneNumber, " +
                        "users.email, " +
                        "users.position, " +
                        "warehouses.warehouseName " +
                        "FROM warehouse_users " +
                        "INNER JOIN users ON (users.userID = warehouse_users.userID)" +
                        "INNER JOIN warehouses ON (warehouses.warehouseID = warehouse_users.warehouseID)" +
                        "WHERE warehouse_users.userID = ?;";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        warehouseUser[0] = new WarehouseUser();
                        warehouseUser[0].setUserId(resultSet.getInt("userID"));
                        Integer warehouseId = resultSet.getInt("warehouseID");
                        Warehouse warehouse = warehouseGenericDao.getById(warehouseId);
                        warehouseUser[0].setLogin(resultSet.getString("userLogin"));
                        warehouseUser[0].setSalt(resultSet.getString("salt"));
                        warehouseUser[0].setPassAndSalt(resultSet.getString("passAndSalt"));
                        warehouseUser[0].setUserRole(User.UserRole.valueOf(resultSet.getString("userRoleID")));
                        warehouseUser[0].setUserName(resultSet.getString("userName"));
                        warehouseUser[0].setPhoneNumber(resultSet.getString("phoneNumber"));
                        warehouseUser[0].setEmail(resultSet.getString("email"));
                        warehouseUser[0].setPosition(resultSet.getString("position"));
                        warehouseUser[0].setWarehouse(warehouse);
                    }
                }
            }
        });
        return warehouseUser[0];
    }

    @Override
    public Collection<WarehouseUser> getAll() throws DaoException {
        final Set<WarehouseUser> result = new HashSet<>();
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT warehouse_users.userID, " +
                        "warehouse_users.warehouseID, " +
                        "users.userLogin, " +
                        "users.salt, " +
                        "users.passAndSalt, " +
                        "users.userRoleID, " +
                        "users.userName, " +
                        "users.phoneNumber, " +
                        "users.email, " +
                        "users.position, " +
                        "warehouses.warehouseName " +
                        "FROM warehouse_users " +
                        "INNER JOIN users ON (users.userID = warehouse_users.userID) " +
                        "INNER JOIN warehouses ON (warehouses.warehouseID = warehouse_users.warehouseID);";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        WarehouseUser warehouseUser = new WarehouseUser();
                        warehouseUser.setUserId(resultSet.getInt("userID"));
                        Integer warehouseId = resultSet.getInt("warehouseID");
                        Warehouse warehouse = warehouseGenericDao.getById(warehouseId);
                        warehouseUser.setLogin(resultSet.getString("userLogin"));
                        warehouseUser.setSalt(resultSet.getString("salt"));
                        warehouseUser.setPassAndSalt(resultSet.getString("passAndSalt"));
                        warehouseUser.setUserRole(User.UserRole.valueOf(resultSet.getString("userRoleID")));
                        warehouseUser.setUserName(resultSet.getString("userName"));
                        warehouseUser.setPhoneNumber(resultSet.getString("phoneNumber"));
                        warehouseUser.setEmail(resultSet.getString("email"));
                        warehouseUser.setPosition(resultSet.getString("position"));
                        warehouseUser.setWarehouse(warehouse);
                        result.add(warehouseUser);
                    }
                }
            }
        });
        return result;
    }

    @Override
    public void deleteById(final Integer id) throws DaoException {
        userDao.deleteById(id);
    }

    @Override
    public Integer saveOrUpdate(final WarehouseUser warehouseUser) throws DaoException {
        final Integer[] result = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                Integer userId = userDao.saveOrUpdate(warehouseUser);
                // if was inserted
                if (userId != null) {
                    String sql = "INSERT INTO warehouse_users VALUES (?, ?)";
                    try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                        statement.setInt(1, userId);
                        statement.setInt(2, warehouseUser.getWarehouse().getWarehouseId());
                        statement.execute();
                    }
                }
            }
        });
        return result[0];
    }

    @Override
    public Integer save(final WarehouseUser warehouseUser) throws DaoException {
        final Integer userId = userDao.save(warehouseUser);
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO warehouse_users VALUES (?, ?)";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)) {
                    statement.setInt(1, userId);
                    statement.setInt(2, warehouseUser.getWarehouse().getWarehouseId());
                    statement.execute();
                }
            }
        });
        return userId;
    }

    @Override
    public void update(final WarehouseUser warehouseUser) throws DaoException {
        userDao.update(warehouseUser);
    }
}
