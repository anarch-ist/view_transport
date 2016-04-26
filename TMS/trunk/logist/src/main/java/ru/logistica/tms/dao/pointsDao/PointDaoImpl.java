package ru.logistica.tms.dao.pointsDao;



import ru.logistica.tms.dao.utils.DaoException;
import ru.logistica.tms.dao.utils.GenericDao;
import ru.logistica.tms.dao.utils.ConnectionManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;

public class PointDaoImpl implements GenericDao<Point> {

    @Override
    public Point getById(final Integer id) throws DaoException {
        final Point point = new Point();
        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "SELECT * from points WHERE pointID = ?";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql)){
                    statement.setInt(1, id);
                    ResultSet resultSet = statement.executeQuery();
                    resultSet.next();
                    point.setPointId(resultSet.getInt("pointID"));
                    point.setPointName(resultSet.getString("pointName"));
                    point.setRegion(resultSet.getString("region"));
                    point.setDistrict(resultSet.getString("district"));
                    point.setLocality(resultSet.getString("locality"));
                    point.setMailIndex(resultSet.getString("mailIndex"));
                    point.setAddress(resultSet.getString("address"));
                    point.setEmail(resultSet.getString("email"));
                    point.setPhoneNumber(resultSet.getString("phoneNumber"));
                    point.setResponsiblePersonId(resultSet.getString("responsiblePersonId"));
                }
            }
        });
        return point;
    }

    @Override
    public Integer saveOrUpdate(final Point point) throws DaoException {
        final Integer[] result = {null};
        ConnectionManager.runWithExceptionRedirect(new ConnectionManager.Exec() {
            @Override
            public void execute() throws Exception {
                String sql = "INSERT INTO points VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON CONFLICT (pointID) DO UPDATE SET pointID = EXCLUDED.pointID, " +
                        "pointName = EXCLUDED.pointName, region = EXCLUDED.region, district = EXCLUDED.district, locality = EXCLUDED.locality, mailIndex = EXCLUDED.mailIndex, " +
                        "address = EXCLUDED.address, email = EXCLUDED.email, phoneNumber = EXCLUDED.phoneNumber, responsiblePersonId = EXCLUDED.responsiblePersonId";
                try (PreparedStatement statement = ConnectionManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    statement.setInt(1, (int)point.getPointId());
                    statement.setString(2, point.getPointName());
                    statement.setString(3, point.getRegion());
                    statement.setString(4, point.getDistrict());
                    statement.setString(5, point.getLocality());
                    statement.setString(6, point.getMailIndex());
                    statement.setString(7, point.getAddress());
                    statement.setString(8, point.getEmail());
                    statement.setString(9, point.getPhoneNumber());
                    statement.setString(10, point.getResponsiblePersonId());
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
    public Integer save(Point entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void update(Point entity) throws DaoException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Collection<Point> getAll() throws DaoException {
        throw new NotImplementedException();
    }

    @Override
    public void deleteById(Integer id) throws DaoException {
        throw new NotImplementedException();
    }
}
