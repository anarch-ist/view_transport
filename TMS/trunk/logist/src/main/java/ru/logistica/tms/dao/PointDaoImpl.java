package ru.logistica.tms.dao;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PointDaoImpl implements KeyDifferenceDao<Point>{
//    KeyDifferenceDao keyDifferenceDao = new PointDaoImpl();

    @Override
    public Point getKeyDifferenceById(Integer id) throws SQLException {
            Point point = new Point();
            String sql = "SELECT * from points WHERE pointID = ?";
            try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql)){
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
            return point;
    }

    @Override
    public Integer saveOrUpdateKeyDifference(Point point) throws SQLException {
        Integer result = null;
        String sql = "INSERT INTO points VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (pointID) DO UPDATE SET pointID = EXCLUDED.pointID, " +
                "pointName = EXCLUDED.pointName, region = EXCLUDED.region, district = EXCLUDED.district, locality = EXCLUDED.locality, mailIndex = EXCLUDED.mailIndex, " +
                "address = EXCLUDED.address, email = EXCLUDED.email, phoneNumber = EXCLUDED.phoneNumber, responsiblePersonId = EXCLUDED.responsiblePersonId";
        try (PreparedStatement statement = JdbcUtil.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
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
                result = resultSet.getInt(1);
            }
        }
        return result;
    }
}
