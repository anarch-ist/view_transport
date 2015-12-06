package ru.logist.sbat;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.sql.*;

public class DataBase {
    private Connection connection;

    public DataBase(String url, String dbName, String user, String password) {
        // create connection to dbName
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    url+dbName,
                    user,
                    password
            );
            connection.setAutoCommit(true);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return inserted number of rows
     * @throws SQLException
     */
    public int insert(String insertQuery) throws SQLException {
        Statement statement = connection.createStatement();
        int result = statement.executeUpdate(insertQuery);
        statement.close();
        return result;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public void generateInsertIntoRequestTable() throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            String sql =
                    "INSERT INTO requests (requestID, requestNumber, date, marketAgentUserID, clientID, destinationPointID)\n" +
                            "  SELECT\n" +
                            "    NULL,\n" +
                            "    ?,\n" +
                            "    now(),\n" +
                            "    users.userID     AS randomMarketAgentUserID,\n" +
                            "    clients.clientID AS randomClientID,\n" +
                            "    points.pointID   AS randomAgencyPointID\n" +
                            "  FROM users\n" +
                            "    INNER JOIN (clients, points)\n" +
                            "  WHERE users.userRoleID = 'MARKET_AGENT' AND points.pointTypeID = 'AGENCY'\n" +
                            "  ORDER BY RAND()\n" +
                            "  LIMIT 1;";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, RandomStringUtils.randomAlphanumeric(12));
            preparedStatement.executeUpdate();
            System.out.println("insert into requests successfully executed!");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public void generateInsertIntoRouteListsTable() throws SQLException {
        PreparedStatement preparedStatement = null;
        try {
            String sql =
                    "INSERT INTO route_lists\n" +
                    "  SELECT NULL , ?, ?, ?, ?, ?, routeID\n" +
                    "  FROM routes\n" +
                    "  ORDER BY RAND()\n" +
                    "  LIMIT 1;\n";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, RandomStringUtils.randomAlphanumeric(12));
            preparedStatement.setInt(2, RandomUtils.nextInt(1, 20));
            preparedStatement.setString(3, "driver_" + RandomStringUtils.randomAlphabetic(3));
            preparedStatement.setString(4, RandomStringUtils.randomNumeric(10));
            preparedStatement.setString(5, RandomStringUtils.randomAlphanumeric(6));
            preparedStatement.executeUpdate();
            System.out.println("insert into route_lists successfully executed!");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

    }

}
