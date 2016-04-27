package ru.logistica.tms.dao.usersDao;


import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDaoImpl implements GenericUserDao<User> {

    @Override
    public Integer saveOrUpdate(final User user) throws DaoException {
        final Integer[] result = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO users (userID, userLogin, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (userLogin) DO UPDATE SET " +
                        "userLogin = EXCLUDED.userLogin, salt = EXCLUDED.salt, passAndSalt = EXCLUDED.passAndSalt, " +
                        "userRoleID = EXCLUDED.userRoleID, userName = EXCLUDED.userName, " +
                        "phoneNumber = EXCLUDED.phoneNumber, email = EXCLUDED.email, position = EXCLUDED.position";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setString(1, user.getLogin());
                    statement.setString(2, user.getSalt());
                    statement.setString(3, user.getPassAndSalt());
                    statement.setString(4, ConstantCollections.getUserRoleIdByUserRole(user.getUserRole()));
                    statement.setString(5, user.getUserName());
                    statement.setString(6, user.getPhoneNumber());
                    statement.setString(7, user.getEmail());
                    statement.setString(8, user.getPosition());
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
    public Integer save(final User user) throws DaoException {
        final Integer[] result = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO users (userLogin, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setString(1, user.getLogin());
                    statement.setString(2, user.getSalt());
                    statement.setString(3, user.getPassAndSalt());
                    statement.setString(4, ConstantCollections.getUserRoleIdByUserRole(user.getUserRole()));
                    statement.setString(5, user.getUserName());
                    statement.setString(6, user.getPhoneNumber());
                    statement.setString(7, user.getEmail());
                    statement.setString(8, user.getPosition());
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
    public void update(final User user) throws DaoException {
        final int[] countOfUpdated = {0};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "UPDATE users SET salt = ?, passAndSalt = ?, userRoleID = ?, userName = ?, " +
                        "phoneNumber = ?, email = ?, position = ? WHERE users.userLogin = ?";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, user.getSalt());
                    statement.setString(2, user.getPassAndSalt());
                    statement.setString(3, ConstantCollections.getUserRoleIdByUserRole(user.getUserRole()));
                    statement.setString(4, user.getUserName());
                    statement.setString(5, user.getPhoneNumber());
                    statement.setString(6, user.getEmail());
                    statement.setString(7, user.getPosition());
                    statement.setString(8, user.getLogin());
                    statement.execute();
                    countOfUpdated[0] = statement.getUpdateCount();
                }
            }
        });

        if(countOfUpdated[0] == 0){
            throw new DaoException("The update has not occurred, such login '" + user.getLogin() + "' does not exist in table users");
        }
    }

    @Override
    public void deleteUserByLogin(final String login) throws DaoException {
        final int[] countOfUpdated = {0};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "DELETE FROM users WHERE userLogin = ?";
                try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
                    preparedStatement.setString(1, login);
                    preparedStatement.execute();
                    countOfUpdated[0] = preparedStatement.getUpdateCount();
                }
            }
        });

        if(countOfUpdated[0] == 0){
            throw new DaoException("The delete has not occurred, such login '" + login + "' does not exist in table users");
        }
    }

    @Override
    public User getById(final Integer id) throws DaoException {
        final User[] user = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from users WHERE userID = ?";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();
                    if(resultSet.next()) {
                        user[0] = new User();
                        user[0].setUserId(resultSet.getInt("userID"));
                        user[0].setLogin(resultSet.getString("userLogin"));
                        user[0].setSalt(resultSet.getString("salt"));
                        user[0].setPassAndSalt(resultSet.getString("passAndSalt"));
                        String userRoleId = resultSet.getString("userRoleID");
                        user[0].setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
                        user[0].setUserName(resultSet.getString("userName"));
                        user[0].setPhoneNumber(resultSet.getString("phoneNumber"));
                        user[0].setEmail(resultSet.getString("email"));
                        user[0].setPosition(resultSet.getString("position"));
                    }
                }
            }
        });
        return user[0];
    }

    @Override
    public User getByLogin(final String login) throws DaoException {
        final User[] user = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from users WHERE userLogin = ?";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setString(1, login);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        user[0] = new User();
                        user[0].setUserId(resultSet.getInt("userID"));
                        user[0].setLogin(resultSet.getString("userLogin"));
                        user[0].setSalt(resultSet.getString("salt"));
                        user[0].setPassAndSalt(resultSet.getString("passAndSalt"));
                        user[0].setUserRole(ConstantCollections.getUserRoleByUserRoleId(resultSet.getString("userRoleID")));
                        user[0].setUserName(resultSet.getString("userName"));
                        user[0].setPhoneNumber(resultSet.getString("phoneNumber"));
                        user[0].setEmail(resultSet.getString("email"));
                        user[0].setPosition(resultSet.getString("position"));
                    }
                }
            }
        });
        return user[0];
    }

    @Override
    public Collection<User> getAll() throws DaoException {
        final Set<User> result = new HashSet<>();

        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {

                String sql = "SELECT * from users";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)) {
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        User user = new User();
                        user.setUserId(resultSet.getInt("userID"));
                        user.setLogin(resultSet.getString("userLogin"));
                        user.setSalt(resultSet.getString("salt"));
                        user.setPassAndSalt(resultSet.getString("passAndSalt"));
                        String userRoleId = resultSet.getString("userRoleID");
                        user.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
                        user.setUserName(resultSet.getString("userName"));
                        user.setPhoneNumber(resultSet.getString("phoneNumber"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPosition(resultSet.getString("position"));
                        result.add(user);
                    }

                }

            }
        });
        return result;
    }

    @Override
    public void deleteById(final Integer id) throws DaoException {
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "DELETE FROM users WHERE userID = ?";
                try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                }
            }
        });
    }

}
