package ru.logist.sbat.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final Logger logger = LogManager.getLogger();
    private volatile Connection connection;

    public DBManager(Connection connection) {
        this.connection = connection;
    }

    public void close() {
        DBUtils.closeConnectionQuietly(connection);
    }

    /**
     * write all JSON data into database inside one transaction or ROLLBACK if error.
     * @return never returns null
     * @param dataFrom1c
     */
    public InsertOrUpdateResult updateDataFromJSONObject(DataFrom1c dataFrom1c) {
        InsertOrUpdateTransactionScript insertOrUpdateTransactionScript = new InsertOrUpdateTransactionScript(connection, dataFrom1c);
        return insertOrUpdateTransactionScript.updateData();
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
                        || tableName.equals("request_statuses")
                        || tableName.equals("request_statuses_for_user_role")
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
            connection.createStatement().executeUpdate(
                    "INSERT INTO users (userID, userIDExternal, dataSourceID, login, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position, pointID, clientID)\n" +
                            "  VALUES\n" +
                            "  (1, 'eebrfiebreiubritbvritubvriutbv', 'ADMIN_PAGE', 'parser', 'nvuritneg4785231', md5(CONCAT(md5('nolpitf43gwer'), 'nvuritneg4785231')),\n" +
                            "   'ADMIN', 'parser', '', 'fff@fff', '', NULL, NULL),\n" +
                            "  (2, 'eebrfiebreiubrrervritubvriutbv', 'ADMIN_PAGE', 'test', 'nvuritneg4785231', md5(CONCAT(md5('test'), 'nvuritneg4785231')),\n" +
                            "   'ADMIN', 'ivanov i.i.', '904534356', 'test@test.ru', 'position', NULL, NULL);");

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
    public Connection getConnection() {
        return connection;
    }
}
