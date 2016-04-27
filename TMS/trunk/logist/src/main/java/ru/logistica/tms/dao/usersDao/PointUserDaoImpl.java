package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.DaoException;
import ru.logistica.tms.dao.GenericDao;
import ru.logistica.tms.dao.ConnectionManager;
import ru.logistica.tms.dao.pointsDao.Point;
import ru.logistica.tms.dao.pointsDao.PointDaoImpl;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;
import ru.logistica.tms.dao.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PointUserDaoImpl implements GenericUserDao<PointUser> {

    private GenericUserDao<User> userDao = new UserDaoImpl();
    private GenericDao<Point> pointDao = new PointDaoImpl();

    @Override
    public PointUser getByLogin(final String login) throws DaoException {
        User user = userDao.getByLogin(login);
        if(user == null){
            return null;
        }else {
            Integer userId = userDao.getByLogin(login).getUserId();
            PointUser pointUser = getById(userId);
            return pointUser;
        }
    }

    @Override
    public void deleteUserByLogin(final String login) throws DaoException {
        userDao.deleteUserByLogin(login);
    }

    @Override
    public PointUser getById(final Integer id) throws DaoException {
        final PointUser[] pointUser = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT point_users.userID, " +
                        "point_users.pointID, " +
                        "users.userLogin, " +
                        "users.salt, " +
                        "users.passAndSalt, " +
                        "users.userRoleID, " +
                        "users.userName, " +
                        "users.phoneNumber, " +
                        "users.email, " +
                        "users.position, " +
                        "points.pointName, " +
                        "points.region, " +
                        "points.district," +
                        "points.locality, " +
                        "points.mailIndex, " +
                        "points.address, " +
                        "points.email, " +
                        "points.phoneNumber, " +
                        "points.responsiblePersonID FROM point_users " +
                        "INNER JOIN users ON (users.userID = point_users.userID)" +
                        "INNER JOIN points ON (points.pointID = point_users.pointID)" +
                        "WHERE point_users.userID = ?;";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        pointUser[0] = new PointUser();
                        pointUser[0].setUserId(resultSet.getInt("userID"));
                        Integer pointId = resultSet.getInt("pointID");
                        Point point = pointDao.getById(pointId);
                        pointUser[0].setLogin(resultSet.getString("userLogin"));
                        pointUser[0].setSalt(resultSet.getString("salt"));
                        pointUser[0].setPassAndSalt(resultSet.getString("passAndSalt"));
                        String userRoleId = resultSet.getString("userRoleID");
                        pointUser[0].setUserRole(ConstantCollections.getUserRoleByUserRoleId(userRoleId));
                        pointUser[0].setUserName(resultSet.getString("userName"));
                        pointUser[0].setPhoneNumber(resultSet.getString("phoneNumber"));
                        pointUser[0].setEmail(resultSet.getString("email"));
                        pointUser[0].setPosition(resultSet.getString("position"));
                        pointUser[0].setPoint(point);
                    }
                }
            }
        });
        return pointUser[0];
    }

    @Override
    public Collection<PointUser> getAll() throws DaoException {
        final Set<PointUser> result = new HashSet<>();
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT point_users.userID, " +
                        "point_users.pointID, " +
                        "users.userLogin, " +
                        "users.salt, " +
                        "users.passAndSalt, " +
                        "users.userRoleID, " +
                        "users.userName, " +
                        "users.phoneNumber, " +
                        "users.email, " +
                        "users.position, " +
                        "points.pointName, " +
                        "points.region, " +
                        "points.district," +
                        "points.locality, " +
                        "points.mailIndex, " +
                        "points.address, " +
                        "points.email, " +
                        "points.phoneNumber, " +
                        "points.responsiblePersonID FROM point_users " +
                        "INNER JOIN users ON (users.userID = point_users.userID) " +
                        "INNER JOIN points ON (points.pointID = point_users.pointID);";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()){
                        PointUser pointUser = new PointUser();
                        pointUser.setUserId(resultSet.getInt("userID"));
                        Integer pointId = resultSet.getInt("pointID");
                        Point point = pointDao.getById(pointId);
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
            }
        });
        return result;
    }

    @Override
    public void deleteById(final Integer id) throws DaoException {
        userDao.deleteById(id);
    }

    @Override
    public Integer saveOrUpdate(final PointUser pointUser) throws DaoException {
        final Integer[] result = {null};
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                Integer userId = userDao.saveOrUpdate(pointUser);
                // if was inserted
                if (userId != null) {
                    String sql = "INSERT INTO point_users VALUES (?, ?)";
                    try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                        statement.setInt(1, userId);
                        statement.setInt(2, (Integer) pointUser.getPoint().getPointId());
                        statement.execute();
                    }
                }
            }
        });
        return result[0];
    }

    @Override
    public Integer save(final PointUser pointUser) throws DaoException {
        final Integer userId = userDao.save(pointUser);
        Utils.runWithExceptionRedirect(new Utils.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO point_users VALUES (?, ?)";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)) {
                    statement.setInt(1, userId);
                    statement.setInt(2, (Integer) pointUser.getPoint().getPointId());
                    statement.execute();
                }
            }
        });
        return userId;
    }

    @Override
    public void update(final PointUser pointUser) throws DaoException {
        userDao.update(pointUser);
    }
}
