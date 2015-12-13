package ru.logist.sbat;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class DataBase {
    private static final Logger logger = LogManager.getLogger(DataBase.class);
    private Connection connection;
    private List<String> invoiceStatuses;

    public DataBase(String url, String dbName, String user, String password) {
        // create connection to dbName
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    url + dbName,
                    user,
                    password
            );
            connection.setAutoCommit(true);
            invoiceStatuses = getInvoiceStatuses();
        } catch (ClassNotFoundException | SQLException e) {
            logger.debug(e);
            closeConnectionQuietly();
        }
    }

    public List<String> getInvoiceStatuses() throws SQLException {
        List<String> result = new ArrayList<>();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM invoice_statuses WHERE sequence >= 0 ORDER BY sequence;");
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return result;
    }

    public void closeConnectionQuietly() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            logger.trace("random insert into requests");
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
            logger.trace("random insert into route lists");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

    }

    public void generateInsertIntoInvoicesTable() throws SQLException{
        PreparedStatement preparedStatement = null;
        try {
            String sql =
                    "INSERT INTO invoices\n" +
                            "  SELECT NULL, ?, ?, now(), now(), ?, ?, ?, ?, 'CREATED', NULL, requestID, pointID as warehousePointID, NULL , NULL\n" +
                            "  FROM requests\n" +
                            "    INNER JOIN (points)\n" +
                            "  WHERE points.pointTypeID = 'WAREHOUSE'\n" +
                            "  ORDER BY RAND()\n" +
                            "  LIMIT 1;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, RandomStringUtils.randomAlphanumeric(12));
            preparedStatement.setString(2, RandomStringUtils.randomAlphanumeric(12));
            preparedStatement.setInt(3, RandomUtils.nextInt(1, 20));
            preparedStatement.setInt(4, RandomUtils.nextInt(1, 20));
            preparedStatement.setInt(5, RandomUtils.nextInt(1, 10_000));
            preparedStatement.setDouble(6, RandomUtils.nextDouble(4000.0, 50_000.0));

            preparedStatement.executeUpdate();
            logger.trace("random insert into invoices");
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public void generateUpdateInvoiceStatuses() throws SQLException {
        Statement getCurrentStatusStm = null;
        Statement updateStatusStm = null;
        try {
            getCurrentStatusStm = connection.createStatement();
            ResultSet rs = getCurrentStatusStm.executeQuery("SELECT invoiceID, invoiceStatusID FROM invoices ORDER BY RAND() LIMIT 1;");
            rs.next();
            int randId = rs.getInt("invoiceID");
            String currentStatus = rs.getString("invoiceStatusID");
            int currentIndex = invoiceStatuses.indexOf(currentStatus);
            int nextStatusIndex = currentIndex + 1;
            if (currentIndex == -1 || (nextStatusIndex > invoiceStatuses.size() - 1)) return;
            String nextStatus = invoiceStatuses.get(nextStatusIndex);

            updateStatusStm = connection.createStatement();
            updateStatusStm.executeUpdate("UPDATE invoices SET invoiceStatusID = " + "'" +nextStatus+"'" + " WHERE invoiceID = " + randId);

            logger.trace("invoiceID = " + randId + "lastStatus = " + currentStatus + "newStatus = " + nextStatus);
        } finally {
            if (getCurrentStatusStm != null) {
                getCurrentStatusStm.close();
            }
            if (updateStatusStm != null) {
                updateStatusStm.close();
            }
        }

    }
}
