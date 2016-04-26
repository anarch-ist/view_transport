package ru.logistica.tms.dao.usersDao;


import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.ConnectionManager;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AbstractUserDaoImpl implements GenericUserDao<AbstractUser> {

    @Override
    public Integer saveOrUpdate(final AbstractUser user) throws DaoException {
        final Integer[] result = {null};
        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO abstract_users (userID, userLogin, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position) " +
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
    public Integer save(final AbstractUser user) throws DaoException {
        final Integer[] result = {null};
        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO abstract_users (userLogin, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position) " +
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
    public void update(final AbstractUser user) throws DaoException {
        final int[] countOfUpdated = {0};
        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "UPDATE abstract_users SET salt = ?, passAndSalt = ?, userRoleID = ?, userName = ?, " +
                        "phoneNumber = ?, email = ?, position = ? WHERE abstract_users.userLogin = ?";
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
            throw new DaoException("The update has not occurred, such login '" + user.getLogin() + "' does not exist in table abstract_users");
        }
    }

    @Override
    public void deleteUserByLogin(final String login) throws DaoException {
        final int[] countOfUpdated = {0};
        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "DELETE FROM abstract_users WHERE userLogin = ?";
                try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
                    preparedStatement.setString(1, login);
                    preparedStatement.execute();
                    countOfUpdated[0] = preparedStatement.getUpdateCount();
                }
            }
        });

        if(countOfUpdated[0] == 0){
            throw new DaoException("The delete has not occurred, such login '" + login + "' does not exist in table abstract_users");
        }
    }

    @Override
    public AbstractUser getById(final Integer id) throws DaoException {
        final AbstractUser[] abstractUser = {null};
        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from abstract_users WHERE userID = ?";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();
                    if(resultSet.next()) {
                        abstractUser[0] = new AbstractUser();
                        abstractUser[0].setUserId(resultSet.getInt("userID"));
                        abstractUser[0].setLogin(resultSet.getString("userLogin"));
                        abstractUser[0].setSalt(resultSet.getString("salt"));
                        abstractUser[0].setPassAndSalt(resultSet.getString("passAndSalt"));
                        String userRoleId = resultSet.getString("userRoleID");
                        abstractUser[0].setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
                        abstractUser[0].setUserName(resultSet.getString("userName"));
                        abstractUser[0].setPhoneNumber(resultSet.getString("phoneNumber"));
                        abstractUser[0].setEmail(resultSet.getString("email"));
                        abstractUser[0].setPosition(resultSet.getString("position"));
                    }
                }
            }
        });
        return abstractUser[0];
    }

    @Override
    public AbstractUser getByLogin(final String login) throws DaoException {
        final AbstractUser[] abstractUser = {null};
        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from abstract_users WHERE userLogin = ?";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setString(1, login);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        abstractUser[0] = new AbstractUser();
                        abstractUser[0].setUserId(resultSet.getInt("userID"));
                        abstractUser[0].setLogin(resultSet.getString("userLogin"));
                        abstractUser[0].setSalt(resultSet.getString("salt"));
                        abstractUser[0].setPassAndSalt(resultSet.getString("passAndSalt"));
                        abstractUser[0].setUserRole(ConstantCollections.getUserRoleByUserRoleId(resultSet.getString("userRoleID")));
                        abstractUser[0].setUserName(resultSet.getString("userName"));
                        abstractUser[0].setPhoneNumber(resultSet.getString("phoneNumber"));
                        abstractUser[0].setEmail(resultSet.getString("email"));
                        abstractUser[0].setPosition(resultSet.getString("position"));
                    }
                }
            }
        });
        return abstractUser[0];
    }

    @Override
    public Collection<AbstractUser> getAll() throws DaoException {
        final Set<AbstractUser> result = new HashSet<>();

        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {

                String sql = "SELECT * from abstract_users";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)) {
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        AbstractUser abstractUser = new AbstractUser();
                        abstractUser.setUserId(resultSet.getInt("userID"));
                        abstractUser.setLogin(resultSet.getString("userLogin"));
                        abstractUser.setSalt(resultSet.getString("salt"));
                        abstractUser.setPassAndSalt(resultSet.getString("passAndSalt"));
                        String userRoleId = resultSet.getString("userRoleID");
                        abstractUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
                        abstractUser.setUserName(resultSet.getString("userName"));
                        abstractUser.setPhoneNumber(resultSet.getString("phoneNumber"));
                        abstractUser.setEmail(resultSet.getString("email"));
                        abstractUser.setPosition(resultSet.getString("position"));
                        result.add(abstractUser);
                    }

                }

            }
        });
        return result;
    }

    @Override
    public void deleteById(final Integer id) throws DaoException {
        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "DELETE FROM abstract_users WHERE userID = ?";
                try (PreparedStatement preparedStatement = ConnectionManager.getConnection().prepareStatement(sql)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                }
            }
        });
    }

}
