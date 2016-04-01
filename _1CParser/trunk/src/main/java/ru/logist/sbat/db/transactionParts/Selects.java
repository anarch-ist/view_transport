package ru.logist.sbat.db.transactionParts;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Selects {
    private static Connection connection;

    public static void setConnection(Connection connection) {
        Selects.connection = connection;
    }

    /**
     *
     * @return All pointIDExternals and pointNames from dataBase as map.
     * @throws SQLException
     */
    static Map<String, String> selectAllPoints() throws SQLException {
        Map<String, String> allPoints = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT pointIDExternal, pointName FROM points;");
        while (resultSet.next()) {
            allPoints.put(resultSet.getString(1), resultSet.getString(2));
        }
        return allPoints;
    }

    static Set<String> selectAllClientsIdExternal() throws SQLException {
        Set<String> result = new HashSet<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT clientIDExternal FROM clients;");
        while (resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        return result;
    }

    static Set<String> selectAllUsersIdExternal(String dataSourceId) throws SQLException {
        Set<String> result = new HashSet<>();
        PreparedStatement statement = connection.prepareStatement("SELECT userIDExternal FROM users WHERE dataSourceID = ?");
        statement.setString(1, dataSourceId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        return result;
    }

    /**
     * String is userIdExternal key, Integer is userId
     * @param dataSourceId
     * @return
     * @throws SQLException
     */
    static BidiMap<String, Integer> selectAllUsersAsKeyPairs(String dataSourceId) throws SQLException {
        BidiMap<String, Integer> result = new DualHashBidiMap<>();
        PreparedStatement statement = connection.prepareStatement("SELECT userIDExternal, userID FROM users WHERE dataSourceID = ?");
        statement.setString(1, dataSourceId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            result.put(resultSet.getString(1), resultSet.getInt(2));
        }
        return result;
    }

    /**
     * String is directionIDExternal key, Integer is routeID
     * @param dataSourceId
     * @return
     * @throws SQLException
     */
    static BidiMap<String, Integer> selectAllRoutesAsKeyPairs(String dataSourceId) throws SQLException {
        BidiMap<String, Integer> result = new DualHashBidiMap<>();
        PreparedStatement statement = connection.prepareStatement("SELECT directionIDExternal, routeID FROM routes WHERE dataSourceID = ?");
        statement.setString(1, dataSourceId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            result.put(resultSet.getString(1), resultSet.getInt(2));
        }
        return result;
    }

    /**
     *
     * @return All directionIDExternal and routeNames from dataBase as bidimap.
     * @throws SQLException
     */
    static BidiMap<String, String> selectAllRoutesAsExtKeyAndName(String dataSourceId) throws SQLException {
        //
        BidiMap<String, String> allRoutes = new DualHashBidiMap<>();
        PreparedStatement statement = connection.prepareStatement("SELECT directionIDExternal, routeName FROM routes WHERE dataSourceID = ?");
        statement.setString(1, dataSourceId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            allRoutes.put(resultSet.getString(1), resultSet.getString(2));
        }
        return allRoutes;
    }
}
