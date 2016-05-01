package ru.logist.sbat.db.transactionParts;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import ru.logist.sbat.db.DBUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Selects {
    public static final String PARSER_LOGIN = "parser";
    private Connection connection;
    private static Selects INSTANCE;

    private Selects() {}

    public static Selects getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Selects();
        return INSTANCE;
    }

    private Integer parserId;
    private BidiMap<String, Integer> allExchangeKeys;
    private BidiMap<String, String>  allRoutesAsExtKeyAndName;
    private BidiMap<String, Integer> allPointsAsKeyPairs;
    private Map<Integer, String>     allPointsByName;
    private BidiMap<String, Integer> allClientsAsKeyPairs;
    private BidiMap<String, Integer> allUsersAsKeyPairs;
    private BidiMap<String, Integer> allRoutesAsKeyPairs;
    private BidiMap<String, Integer> allRequestsAsKeyPairs;
    private BidiMap<String, Integer> allRouteListsAsKeyPairs;


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void clearCache() {
        parserId = null;
        allExchangeKeys = null;
        allRoutesAsExtKeyAndName = null;
        allPointsAsKeyPairs = null;
        allPointsByName = null;
        allClientsAsKeyPairs = null;
        allUsersAsKeyPairs = null;
        allRoutesAsKeyPairs = null;
        allRequestsAsKeyPairs = null;
        allRouteListsAsKeyPairs = null;
    }




    Integer selectParserId() throws SQLException {
        if (parserId == null)
            try (PreparedStatement statement = connection.prepareStatement("SELECT userID FROM users WHERE login = ?")) {
                statement.setString(1, PARSER_LOGIN);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                parserId = resultSet.getInt(1);
            }
        return parserId;
    }

    /**
     *
     * @return All directionIDExternal and routeNames from dataBase as bidimap.
     * @throws SQLException
     */
    BidiMap<String, String> selectAllRoutesAsExtKeyAndName(String dataSourceId) throws SQLException {
        if (allRoutesAsExtKeyAndName == null) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT directionIDExternal, routeName FROM routes WHERE dataSourceID = ?")) {
                BidiMap<String, String> allRoutes = new DualHashBidiMap<>();

                statement.setString(1, dataSourceId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    allRoutes.put(resultSet.getString(1), resultSet.getString(2));
                }
                allRoutesAsExtKeyAndName = allRoutes;
            }
        }
        return allRoutesAsExtKeyAndName;
    }

    public Map<Integer, String> allPointNamesById(String dataSourceId) throws SQLException {
        if (allPointsByName == null) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT pointID, pointName FROM points WHERE dataSourceID = ?")) {
                Map<Integer, String> allPoints = new HashMap<>();
                statement.setString(1, dataSourceId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    allPoints.put(resultSet.getInt(1), resultSet.getString(2));
                }
                allPointsByName = allPoints;
            }
        }
        return allPointsByName;
    }

    BidiMap<String, Integer> allPointsAsKeyPairs() throws SQLException {
        if (allPointsAsKeyPairs == null)
            allPointsAsKeyPairs = asKeyPairs(DBManager.LOGIST_1C, "pointIDExternal", "pointID", "points");
        return allPointsAsKeyPairs;
    }
    BidiMap<String, Integer> allClientsAsKeyPairs() throws SQLException {
        if (allClientsAsKeyPairs == null)
            allClientsAsKeyPairs = asKeyPairs(DBManager.LOGIST_1C, "clientIDExternal", "clientID", "clients");
        return allClientsAsKeyPairs;
    }
    BidiMap<String, Integer> allUsersAsKeyPairs() throws SQLException {
        if (allUsersAsKeyPairs == null)
            allUsersAsKeyPairs = asKeyPairs(DBManager.LOGIST_1C, "userIDExternal", "userID", "users");
        return allUsersAsKeyPairs;
    }
    BidiMap<String, Integer> allRoutesAsKeyPairs() throws SQLException {
        if (allRoutesAsKeyPairs == null)
            allRoutesAsKeyPairs = asKeyPairs(DBManager.LOGIST_1C, "directionIDExternal", "routeID", "routes");
        return allRoutesAsKeyPairs;
    }
    BidiMap<String, Integer> allRequestsAsKeyPairs() throws SQLException {
        if (allRequestsAsKeyPairs == null)
            allRequestsAsKeyPairs = asKeyPairs(DBManager.LOGIST_1C, "requestIDExternal", "requestID", "requests");
        return allRequestsAsKeyPairs;
    }
    BidiMap<String, Integer> allRouteListsAsKeyPairs() throws SQLException {
        if (allRouteListsAsKeyPairs == null)
            allRouteListsAsKeyPairs = asKeyPairs(DBManager.LOGIST_1C, "routeListIDExternal", "routeListID", "route_lists");
        return allRouteListsAsKeyPairs;
    }

    private BidiMap<String, Integer> asKeyPairs(String dataSourceId, String idExternalName, String idName, String tableName) throws SQLException {
        PreparedStatement statement = null;
        try {
            BidiMap<String, Integer> result = new DualHashBidiMap<>();
            String sql = "SELECT " + idExternalName + ", " + idName + " FROM " + tableName + " WHERE dataSourceID = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, dataSourceId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.put(resultSet.getString(1), resultSet.getInt(2));
            }
            return result;
        } finally {
            DBUtils.closeStatementQuietly(statement);
        }
    }

    /**
     * @return map(serverName, packageNumber)
     * @throws SQLException
     */
    public BidiMap<String, Integer> allExchangeKeys() throws SQLException {
        if (allExchangeKeys == null) {
            PreparedStatement statement = null;
            try {
                BidiMap<String, Integer> result = new DualHashBidiMap<>();
                statement = connection.prepareStatement("SELECT serverName, packageNumber FROM backup_db.exchange;");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    result.put(resultSet.getString(1), resultSet.getInt(2));
                }
                allExchangeKeys = result;
            } finally {
                DBUtils.closeStatementQuietly(statement);
            }
        }
        return allExchangeKeys;
    }


}
