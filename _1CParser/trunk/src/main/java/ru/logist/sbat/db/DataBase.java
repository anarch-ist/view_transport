package ru.logist.sbat.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static final Logger logger = LogManager.getLogger();
    private Connection connection;

    public DataBase(String url, String dbName, String user, String password, String encoding) throws SQLException {
        // create connection to dbName
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    url + dbName + "?" + encoding,
                    user,
                    password
            );
            connection.setAutoCommit(false);

        } catch (ClassNotFoundException | SQLException e) {
            DBUtils.closeConnectionQuietly(connection);
            throw new SQLException(e);
        }
    }

    public void close() {
        DBUtils.closeConnectionQuietly(connection);
    }
    /**
     * write all JSON data into database inside one transaction
     * @param jsonObject
     */
    public void updateDataFromJSONObject(JSONObject jsonObject) throws SQLException {
        InsertOrUpdateTransactionScript insertOrUpdateTransactionScript = new InsertOrUpdateTransactionScript(connection, jsonObject);
        insertOrUpdateTransactionScript.updateData();
    }

    /**
     * this method designed only for tests
     */
    public void truncatePublicTables(){
        Statement statement = null;

        // get All public table names
        DatabaseMetaData md = null;
        try {
            md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            List<String> tableNames = new ArrayList<>();
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }

            connection.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
            for (String tableName : tableNames) {
                if (tableName.equals("user_roles")
                        || tableName.equals("point_types")
                        || tableName.equals("permissions")
                        || tableName.equals("permissions_for_roles")
                        || tableName.equals("invoice_statuses")
                        || tableName.equals("invoice_statuses_for_user_role")
                        || tableName.equals("data_sources")
                        || tableName.equals("route_list_statuses")
                        || tableName.equals("exchange")
                        ) continue;
                statement = connection.createStatement();
                String sql = "TRUNCATE TABLE " + tableName + ";";
                statement.executeUpdate(sql);
                System.out.println(sql);
            }

            connection.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
            connection.createStatement().executeUpdate("INSERT INTO points (pointID, pointIDExternal, dataSourceID, pointName, region, timeZone, docs, comments, openTime, closeTime, district, locality, mailIndex, address, email, phoneNumber, responsiblePersonId, pointTypeID) VALUE\n" +
                    "  (1, 'def', 'LOGIST_1C', 'NO_W_POINT', '', 0, 0, '', '00:00:00', '00:00:00', '', '', '', '', '', '', '', 'WAREHOUSE');");
            connection.createStatement().executeUpdate("INSERT INTO users VALUES (1, 'parser', '', '', '', '','', 'fff@fff', '', 'nvuritneg4785', md5(CONCAT(md5('nolpitf43gwer'), 'nvuritneg4785')), 'ADMIN', NULL);");

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {/*NOPE*/}
            e.printStackTrace();
        }
        finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {/*NOPE*/}
            }
        }


    }

    public void importSQL(InputStream in) throws SQLException {
        SqlRunner sqlRunner = new SqlRunner(connection, new PrintWriter(System.out), new PrintWriter(System.err), true, false);
        sqlRunner.runScript(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

}
