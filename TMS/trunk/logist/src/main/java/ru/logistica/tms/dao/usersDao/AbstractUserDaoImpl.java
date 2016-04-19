package ru.logistica.tms.dao.usersDao;


import ru.logistica.tms.dao.JdbcUtil;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class AbstractUserDaoImpl implements GenericUserDao<AbstractUser> {
    @Override
    public Set<AbstractUser> getAllUsers() throws SQLException {
        String sql = "SELECT * from abstract_users";
        Set<AbstractUser> result = new HashSet<>();
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
        return result;
    }

    @Override
    public AbstractUser getUserById(Integer id) throws SQLException {
        AbstractUser abstractUser = new AbstractUser();
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
        return abstractUser;
    }

    @Override
    public AbstractUser getByLogin(String login) throws SQLException {
        AbstractUser abstractUser = new AbstractUser();
        String sql = "SELECT * from abstract_users WHERE userLogin = ?";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            abstractUser.setUserId(resultSet.getInt("userID"));
            abstractUser.setLogin(resultSet.getString("userLogin"));
            abstractUser.setSalt(resultSet.getString("salt"));
            abstractUser.setPassAndSalt(resultSet.getString("passAndSalt"));
            abstractUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(resultSet.getString("userRoleID")));
            abstractUser.setUserName(resultSet.getString("userName"));
            abstractUser.setPhoneNumber(resultSet.getString("phoneNumber"));
            abstractUser.setEmail(resultSet.getString("email"));
            abstractUser.setPosition(resultSet.getString("position"));
        }
        return abstractUser;
    }

    @Override
    public Integer saveOrUpdateUser(AbstractUser user) throws SQLException {
        Integer result = null;
        String sql = "INSERT INTO abstract_users (userLogin, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (userLogin) DO UPDATE SET " +
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
                result = resultSet.getInt(1);
            }
        }
        return result;
    }


    @Override
    public void deleteUserByLogin(String login) throws SQLException {
        String sql = "DELETE FROM abstract_users WHERE userLogin = ?";
        try (PreparedStatement preparedStatement = JdbcUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            preparedStatement.execute();
        }
    }
}
