package ru.logistica.tms.dao.usersDao;


import ru.logistica.tms.dao.JdbcUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                abstractUser.setLogin(resultSet.getString("login"));
                abstractUser.setSalt(resultSet.getString("salt"));
                abstractUser.setPassAndSalt(resultSet.getString("passAndSalt"));
//                abstractUser.setUserRole(resultSet.getString("userRoleID"));
                abstractUser.setUserName(resultSet.getString("userName"));
                abstractUser.setPhoneNumber(resultSet.getString("phoneNumber"));
                abstractUser.setEmail(resultSet.getString("email"));
                result.add(abstractUser);
            }
        }
        return result;
    }

    @Override
    public AbstractUser getUserById(Integer id) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Integer saveOrUpdateUser(AbstractUser user) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public AbstractUser getByLogin(String string) {
        return null;
    }

    @Override
    public void deleteUserByLogin(String login) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
