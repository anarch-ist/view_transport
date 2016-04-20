package ru.logistica.tms.dao.usersDao;

import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.GenericDao;
import ru.logistica.tms.dao.utils.JdbcUtil;
import ru.logistica.tms.dao.pointsDao.Point;
import ru.logistica.tms.dao.pointsDao.PointDaoImpl;
import ru.logistica.tms.dao.constantsDao.ConstantCollections;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PointUserDaoImpl implements GenericUserDao<PointUser> {

    private GenericUserDao<AbstractUser> abstractUserDao = new AbstractUserDaoImpl();
    private GenericDao<Point> pointDao = new PointDaoImpl();

    @Override
    public PointUser getByLogin(final String login) throws DaoException {
        Integer userId = abstractUserDao.getByLogin(login).getUserId();
        PointUser pointUser = getById(userId);
        return pointUser;
    }

    @Override
    public void deleteUserByLogin(final String login) throws DaoException {
        abstractUserDao.deleteUserByLogin(login);
    }

    @Override
    public PointUser getById(final Integer id) throws DaoException {
        final PointUser pointUser = new PointUser();
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
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
                        "INNER JOIN abstract_users ON (abstract_users.userID = point_users.userID)" +
                        "INNER JOIN points ON (points.pointID = point_users.pointID)" +
                        "WHERE point_users.userID = ?;";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
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
                    }
                }
            }
        });
        return pointUser;
    }

    @Override
    public Collection<PointUser> getAll() throws DaoException {
        final Set<PointUser> result = new HashSet<>();
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
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
                        "INNER JOIN abstract_users ON (abstract_users.userID = point_users.userID) " +
                        "INNER JOIN points ON (points.pointID = point_users.pointID);";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
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
        abstractUserDao.deleteById(id);
    }

    @Override
    public Integer saveOrUpdate(final PointUser pointUser) throws DaoException {
        final Integer[] result = {null};
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                Integer userId = abstractUserDao.saveOrUpdate(pointUser);
                // if was inserted
                if (userId != null) {
                    String sql = "INSERT INTO point_users VALUES (?, ?)";
                    try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
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
        final Integer userId = abstractUserDao.save(pointUser);
        JdbcUtil.runWithExceptionRedirect(new JdbcUtil.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO point_users VALUES (?, ?)";
                try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)) {
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
        abstractUserDao.update(pointUser);
    }
}
