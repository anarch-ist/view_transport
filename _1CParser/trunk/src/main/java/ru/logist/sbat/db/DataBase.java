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
    private volatile Connection connection;

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

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        DBUtils.closeConnectionQuietly(connection);
    }

    /**
     * write all JSON data into database inside one transaction or ROLLBACK if error.
     * @param jsonObject
     */
    public InsertOrUpdateResult updateDataFromJSONObject(JSONObject jsonObject) {
        InsertOrUpdateTransactionScript insertOrUpdateTransactionScript = new InsertOrUpdateTransactionScript(connection, jsonObject);
        insertOrUpdateTransactionScript.updateData();
        return insertOrUpdateTransactionScript.getResult();
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
                        || tableName.equals("big_select")
                        || tableName.equals("all_users")
                        ) continue;
                statement = connection.createStatement();
                String sql = "TRUNCATE TABLE " + tableName + ";";
                statement.executeUpdate(sql);
                System.out.println(sql);
            }

            connection.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
            connection.createStatement().executeUpdate(
                    "INSERT INTO points VALUE (8888, 'w', 'LOGIST_1C', 'point1', 'moscow', 3, 1, 'some_comment1', '9:00:00', '17:00:00', 'some_district', 'efregrthr', '123456',\n" +
                    "'ergersghrth', 'srgf@ewuf.ru', '89032343556', 'resp_personID1', 'WAREHOUSE')");
            connection.createStatement().executeUpdate("INSERT INTO users VALUE (1, 'parser', '', '', 'fff@fff', '', 'nvuritneg4785', md5(CONCAT(md5('nolpitf43gwer'), 'nvuritneg4785')), 'ADMIN', NULL);");
            connection.createStatement().executeUpdate(
                    "INSERT INTO users (login, userName, position, salt, passAndSalt, phoneNumber, email, userRoleID, pointID)\n" +
                    "VALUES\n" +
                    "  ('test', 'ivanov i.i.', 'erwgewg', SUBSTRING(MD5(1) FROM 1 FOR 16), md5(CONCAT(md5('test'), SUBSTRING(MD5(1) FROM 1 FOR 16))),\n" +
                            "'904534356', 'test@test.ru', 'ADMIN', getPointIDByName('point1'))");

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

}
