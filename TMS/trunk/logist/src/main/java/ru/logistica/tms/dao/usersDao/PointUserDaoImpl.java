package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.JdbcUtil;
import ru.logistica.tms.dao.KeyDifferenceDao;
import ru.logistica.tms.dao.Point;
import ru.logistica.tms.dao.PointDaoImpl;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PointUserDaoImpl implements GenericUserDao<PointUser> {

    private GenericUserDao abstractUserDao = new AbstractUserDaoImpl();
    private KeyDifferenceDao<Point> keyDifferenceDao = new PointDaoImpl();

    @Override
    public Set<PointUser> getAllUsers() throws SQLException {
        String sql = "SELECT point_users.userID, " +
                "point_users.pointID, " +
                "abstract_users.userLogin, " +
                "abstract_users.salt, " +
                "abstract_users.passAndSalt, " +
                "abstract_users.userRoleID, " +
                "abstract_users.userName, " +
                "abstract_users.phoneNumber, " +
                "abstract_users.email, " +
                "abstract_users.position, " +
                "points.pointName, " +
                "points.region, " +
                "points.district," +
                "points.locality, " +
                "points.mailIndex, " +
                "points.address, " +
                "points.email, " +
                "points.phoneNumber, " +
                "points.responsiblePersonID FROM point_users " +
                "INNER JOIN abstract_users ON (abstract_users.userID = point_users.userID)\n" +
                "INNER JOIN points ON (points.pointID = point_users.pointID);";
        Set<PointUser> result = new HashSet<>();
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                PointUser pointUser = new PointUser();
                pointUser.setUserId(resultSet.getInt("userID"));
                Integer pointId = resultSet.getInt("pointID");
                Point point = keyDifferenceDao.getKeyDifferenceById(pointId);
                pointUser.setLogin(resultSet.getString("userLogin"));
                pointUser.setSalt(resultSet.getString("salt"));
                pointUser.setPassAndSalt(resultSet.getString("passAndSalt"));
                String userRoleId = resultSet.getString("userRoleID");
                pointUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
                pointUser.setUserName(resultSet.getString("userName"));
                pointUser.setPhoneNumber(resultSet.getString("phoneNumber"));
                pointUser.setEmail(resultSet.getString("email"));
                pointUser.setPosition(resultSet.getString("position"));
                pointUser.setPoint(point);
                result.add(pointUser);
            }
        }
        return result;
    }

    @Override
    public PointUser getUserById(Integer id) throws SQLException {
        PointUser pointUser = new PointUser();
        String sql = "SELECT point_users.userID, " +
                "point_users.pointID, " +
                "abstract_users.userLogin, " +
                "abstract_users.salt, " +
                "abstract_users.passAndSalt, " +
                "abstract_users.userRoleID, " +
                "abstract_users.userName, " +
                "abstract_users.phoneNumber, " +
                "abstract_users.email, " +
                "abstract_users.position, " +
                "points.pointName, " +
                "points.region, " +
                "points.district," +
                "points.locality, " +
                "points.mailIndex, " +
                "points.address, " +
                "points.email, " +
                "points.phoneNumber, " +
                "points.responsiblePersonID FROM point_users " +
                "INNER JOIN abstract_users ON (abstract_users.userID = point_users.userID)\\n\" +\n" +
                "INNER JOIN points ON (points.pointID = point_users.pointID)" +
                "WHERE point_users.userID = ?;";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            pointUser.setUserId(resultSet.getInt("userID"));
            Integer pointId = resultSet.getInt("pointID");
            Point point = keyDifferenceDao.getKeyDifferenceById(pointId);
            pointUser.setLogin(resultSet.getString("userLogin"));
            pointUser.setSalt(resultSet.getString("salt"));
            pointUser.setPassAndSalt(resultSet.getString("passAndSalt"));
            String userRoleId = resultSet.getString("userRoleID");
            pointUser.setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
            pointUser.setUserName(resultSet.getString("userName"));
            pointUser.setPhoneNumber(resultSet.getString("phoneNumber"));
            pointUser.setEmail(resultSet.getString("email"));
            pointUser.setPosition(resultSet.getString("position"));
            pointUser.setPoint(point);
        }
        return pointUser;
    }

    @Override
    public Integer saveOrUpdateUser(PointUser pointUser) throws SQLException {
        Integer userId = abstractUserDao.saveOrUpdateUser(pointUser);
        String sql = "INSERT INTO point_users VALUES (?, ?)";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
            statement.setInt(1, userId);
            statement.setInt(2, (Integer) pointUser.getPoint().getPointId());
            statement.execute();
        }
        return userId;
    }

    @Override
    public PointUser getByLogin(String login) throws SQLException {
        Integer userId = abstractUserDao.getByLogin(login).getUserId();
        PointUser pointUser = getUserById(userId);
        return pointUser;
    }

    @Override
    public Integer deleteUserByLogin(String login) throws SQLException {
        Integer result = abstractUserDao.deleteUserByLogin(login);
        String sql = "DELETE FROM point_users WHERE userID = ?";

        try (PreparedStatement preparedStatement = JdbcUtil.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, result);
            preparedStatement.execute();
        }
        return result;
    }
}
