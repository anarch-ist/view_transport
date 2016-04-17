package ru.logist.sbat.db.transactionParts;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import ru.logist.sbat.db.DBUtils;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;

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

    static Integer selectParserId() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT userID FROM users WHERE login = ?")) {
            statement.setString(1, "parser");
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    /**
     *
     * @return All directionIDExternal and routeNames from dataBase as bidimap.
     * @throws SQLException
     */
    static BidiMap<String, String> selectAllRoutesAsExtKeyAndName(String dataSourceId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT directionIDExternal, routeName FROM routes WHERE dataSourceID = ?")) {
            BidiMap<String, String> allRoutes = new DualHashBidiMap<>();

            statement.setString(1, dataSourceId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                allRoutes.put(resultSet.getString(1), resultSet.getString(2));
            }
            return allRoutes;
        }
    }
    // TODO make cache
    static BidiMap<String, Integer> allPointsAsKeyPairs() throws SQLException {
        return asKeyPairs(InsertOrUpdateTransactionScript.LOGIST_1C, "pointIDExternal", "pointID", "points");
    }
    static BidiMap<String, Integer> allClientsAsKeyPairs() throws SQLException {
        return asKeyPairs(InsertOrUpdateTransactionScript.LOGIST_1C, "clientIDExternal", "clientID", "clients");
    }
    static BidiMap<String, Integer> allUsersAsKeyPairs() throws SQLException {
        return asKeyPairs(InsertOrUpdateTransactionScript.LOGIST_1C, "userIDExternal", "userID", "users");
    }
    static BidiMap<String, Integer> allRoutesAsKeyPairs() throws SQLException {
        return asKeyPairs(InsertOrUpdateTransactionScript.LOGIST_1C, "directionIDExternal", "routeID", "routes");
    }
    public static BidiMap<String, Integer> allRequestsAsKeyPairs() throws SQLException {
        return asKeyPairs(InsertOrUpdateTransactionScript.LOGIST_1C, "requestIDExternal", "requestID", "requests");
    }
    public static BidiMap<String, Integer> allRouteListsAsKeyPairs() throws SQLException {
        return asKeyPairs(InsertOrUpdateTransactionScript.LOGIST_1C, "routeListIDExternal", "routeListID", "route_lists");
    }
    private static BidiMap<String, Integer> asKeyPairs(String dataSourceId, String idExternalName, String idName, String tableName) throws SQLException {
        PreparedStatement statement = null;
        try {
            BidiMap<String, Integer> result = new DualHashBidiMap<>();
            String sql = "SELECT " + idExternalName + ", " + idName + " FROM "+tableName+" WHERE dataSourceID = ?";
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
}
