package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.JdbcUtil;
import ru.logistica.tms.dao.Point;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PointUserDaoImpl implements GenericUserDao<PointUser> {

    private GenericUserDao abstractUserDao = new AbstractUserDaoImpl();

    @Override
    public Set<PointUser> getAllUsers() throws SQLException {
//        String sql = "SELECT points_users.userID, points_users.pointID FROM points_users INNER JOIN (users) ON (users.userID = points_users.userID)";
        String sql = "SELECT * FROM points_users";
        Set<PointUser> result = new HashSet<>();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                PointUser pointUser = new PointUser();
                pointUser.setUserId(resultSet.getInt("userID"));
                Integer pointId = resultSet.getInt("pointID");
                Point point = new Point().getPointById(pointId);
                pointUser.setPoint(point);
                result.add(pointUser);
            }
        }
        return result;
    }

    @Override
    public PointUser getUserById(Integer id) throws SQLException {
        PointUser pointUser = new PointUser();
        String sql = "SELECT * from points_users WHERE userID = ?";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            pointUser.setUserId(resultSet.getInt("userID"));
            Integer pointId = resultSet.getInt("pointID");
            Point point = new Point().getPointById(pointId);
            pointUser.setPoint(point);
        }
        return pointUser;
    }

    @Override
    public Integer saveOrUpdateUser(PointUser pointUser) throws SQLException {
        Integer userId = abstractUserDao.saveOrUpdateUser(pointUser);
        String sql = "INSERT INTO points_users VALUES (?, ?)";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, userId);
            statement.setInt(2, (Integer) pointUser.getPoint().getPointId());
            statement.execute();
        }
        return userId;
    }

    @Override
    public PointUser getByLogin(String login) throws SQLException {
        PointUser pointUser = new PointUser();
        Integer userId = abstractUserDao.getByLogin(login).getUserId();
        String sql = "SELECT * from points_users WHERE userID = ?";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            pointUser.setUserId(resultSet.getInt("userID"));
            Integer pointId = resultSet.getInt("pointID");
            Point point = new Point().getPointById(pointId);
            pointUser.setPoint(point);
        }
        return pointUser;
    }

    @Override
    public Integer deleteUserByLogin(String login) throws SQLException {
        Integer result = abstractUserDao.deleteUserByLogin(login);
        String sql = "DELETE FROM points_users WHERE userID = ?";

        try (PreparedStatement preparedStatement = JdbcUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, result);
            preparedStatement.execute();
        }
        return result;
    }
}
