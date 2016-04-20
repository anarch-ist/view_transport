package ru.logistica.tms.dao.usersDao;


import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.JdbcUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AbstractUserDaoImpl implements GenericUserDao<AbstractUser> {

    @Override
    public AbstractUser getByLogin(final String login) throws DaoException {
        final AbstractUser[] abstractUser = {null};

        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from abstract_users WHERE userLogin = ?";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
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
    public Integer saveOrUpdate(final AbstractUser user) throws DaoException {
        final Integer[] result = {null};
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO abstract_users (userid, userLogin, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (userLogin) DO UPDATE SET " +
                        "userLogin = EXCLUDED.userLogin, salt = EXCLUDED.salt, passAndSalt = EXCLUDED.passAndSalt, userRoleID = EXCLUDED.userRoleID, userName = EXCLUDED.userName, " +
                        "phoneNumber = EXCLUDED.phoneNumber, email = EXCLUDED.email, position = EXCLUDED.position";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
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
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO abstract_users (userLogin, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
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
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "UPDATE abstract_users SET salt = ?, passAndSalt = ?, userRoleID = ?, userName = ?, " +
                        "phoneNumber = ?, email = ?, position = ? WHERE abstract_users.userLogin = ?";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setString(1, user.getSalt());
                    statement.setString(2, user.getPassAndSalt());
                    statement.setString(3, ConstantCollections.getUserRoleIdByUserRole(user.getUserRole()));
                    statement.setString(4, user.getUserName());
                    statement.setString(5, user.getPhoneNumber());
                    statement.setString(6, user.getEmail());
                    statement.setString(7, user.getPosition());
                    statement.setString(8, user.getLogin());
                    statement.execute();
                }
            }
        });
    }

    @Override
    public void deleteUserByLogin(final String login) throws DaoException {
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "DELETE FROM abstract_users WHERE userLogin = ?";
                try (PreparedStatement preparedStatement = JdbcUtil.getConnection().prepareStatement(sql)) {
                    preparedStatement.setString(1, login);
                    preparedStatement.execute();
                }
            }
        });
    }

    @Override
    public AbstractUser getById(final Integer id) throws DaoException {
        final AbstractUser abstractUser = new AbstractUser();
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from abstract_users WHERE userId = ?";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();
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
                }
            }
        });
        return abstractUser;
    }

    @Override
    public Collection<AbstractUser> getAll() throws DaoException {
        final Set<AbstractUser> result = new HashSet<>();

        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {

                String sql = "SELECT * from abstract_users";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
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
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "DELETE FROM abstract_users WHERE userid = ?";
                try (PreparedStatement preparedStatement = JdbcUtil.getConnection().prepareStatement(sql)) {
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                }
            }
        });
    }

}
