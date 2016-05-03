package ru.logist.sbat.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.GlobalUtils;
import ru.logist.sbat.db.transactionParts.*;
import ru.logist.sbat.jsonToBean.beans.DataFrom1c;
import ru.logist.sbat.jsonToBean.beans.PackageData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final Logger logger = LogManager.getLogger();
    public static final String LOGIST_1C = "LOGIST_1C";
    private volatile Connection connection;

    public DBManager(Connection connection) {
        this.connection = connection;
        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } catch (SQLException e) {
            logger.error(e);
            System.exit(-1);
        }
    }

    public void close() {
        DBUtils.closeConnectionQuietly(connection);
    }

    /**
     * write all JSON data into database inside one transaction or ROLLBACK if error.
     * @return never returns null
     * @param dataFrom1c
     */
    public TransactionResult updateDataFromJSONObject(DataFrom1c dataFrom1c) throws DBCohesionException, SQLException {
        String server = dataFrom1c.getServer();

        logger.info(GlobalUtils.getParameterizedString("server = {} \n", server));
        Integer packageNumber = dataFrom1c.getPackageNumber().intValue();
        logger.info(GlobalUtils.getParameterizedString("packageNumber = {}", packageNumber));
        logger.info(GlobalUtils.getParameterizedString("dateCreated = {}", dataFrom1c.getCreated()));

        TransactionResult transactionResult = new TransactionResult();
        transactionResult.setServer(server);
        transactionResult.setPackageNumber(packageNumber);

        TransactionExecutor transactionExecutor = new TransactionExecutor();
        transactionExecutor.setConnection(connection);
        PackageData packageData = dataFrom1c.getPackageData();

        transactionExecutor.put(1, new UpdateExchange(dataFrom1c));

        transactionExecutor.put(2, new DeletePoints(packageData.getDeletePoints()));
        transactionExecutor.put(3, new UpdatePoints(packageData.getUpdatePoints()));

        transactionExecutor.put(4, new DeleteAddresses(packageData.getDeleteAddresses()));
        transactionExecutor.put(5, new UpdatePointsFromAddresses(packageData.getUpdateAddresses()));

        transactionExecutor.put(6, new DeleteRoutes(packageData.getDeleteDirections()));
        transactionExecutor.put(7, new UpdateRoutes(packageData.getUpdateDirections(), packageData.getUpdateRouteLists()));

        transactionExecutor.put(8, new DeleteClients(packageData.getDeleteClients()));
        transactionExecutor.put(9, new UpdateClients(packageData.getUpdateClients()));

        transactionExecutor.put(10, new DeleteTraders(packageData.getDeleteTraders()));
        transactionExecutor.put(11, new UpdateUsersFromTraders(packageData.getUpdateTraders()));

        transactionExecutor.put(12, new UpdateUsersFromClients(packageData.getUpdateClients()));

        transactionExecutor.put(13, new DeleteRouteLists(packageData.getDeleteRouteLists()));
        transactionExecutor.put(14, new UpdateRouteLists(packageData.getUpdateRouteLists()));

        transactionExecutor.put(15, new DeleteRequests(packageData.getDeleteRequests()));
        transactionExecutor.put(16, new UpdateRequests(packageData.getUpdateRequests()));

        transactionExecutor.put(17, new AssignStatusesInRequests(packageData.getUpdateStatuses()));
        transactionExecutor.put(18, new ClearRouteListsInRequests(packageData.getUpdateRouteLists()));
        transactionExecutor.put(19, new AssignRouteListsInRequests(packageData.getUpdateRouteLists()));
        transactionExecutor.put(20, new RefreshMatView());

        try {
            transactionExecutor.executeAll();
            connection.commit();
            transactionResult.setStatus(TransactionResult.OK_STATUS);
            return transactionResult;
        } finally {
            transactionExecutor.closeAll();
        }
    }

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
                        || tableName.equals("all_users")
                        ) continue;
                statement = connection.createStatement();
                String sql = "TRUNCATE TABLE " + tableName + ";";
                statement.executeUpdate(sql);
                System.out.println(sql);
            }
            connection.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");

            connection.createStatement().executeUpdate(
                    "INSERT INTO users (userID, userIDExternal, dataSourceID, login, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position, pointID, clientID)\n" +
                            "  VALUES\n" +
                            "  (1, 'eebrfiebreiubritbvritubvriutbv', 'ADMIN_PAGE', 'parser', 'nvuritneg4785231', md5(CONCAT(md5('nolpitf43gwer'), 'nvuritneg4785231')),\n" +
                            "   'ADMIN', 'parser', '', 'fff@fff', '', NULL, NULL),\n" +
                            "  (2, 'eebrfiebreiubrrervritubvriutbv', 'ADMIN_PAGE', 'test', 'nvuritneg4785231', md5(CONCAT(md5('test'), 'nvuritneg4785231')),\n" +
                            "   'ADMIN', 'ivanov i.i.', '904534356', 'test@test.ru', 'position', NULL, NULL);");

            connection.commit();

        } catch (SQLException e) {
            DBUtils.rollbackQuietly(connection);
        }
        finally {
            DBUtils.closeStatementQuietly(statement);
        }


    }
    public Connection getConnection() {
        return connection;
    }


    public List<String> selectAllFromExchange(Timestamp timestamp) {
        List<String> allData = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("SELECT packageData FROM backup_db.exchange WHERE packageAdded <= ? ORDER BY packageAdded;");
            preparedStatement.setTimestamp(1, timestamp);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                allData.add(resultSet.getString("packageData"));
            }
            connection.commit();
            return allData;
        } catch (SQLException e) {
            DBUtils.rollbackQuietly(connection);
            logger.error(e);
        } finally {
            DBUtils.closeStatementQuietly(preparedStatement);
        }
        return null;
    }

    public void removeFromExchange(Timestamp timestamp) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM backup_db.exchange WHERE packageAdded < ?;");
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            DBUtils.rollbackQuietly(connection);
            logger.error(e);
        } finally {
            DBUtils.closeStatementQuietly(preparedStatement);
        }
    }

    public void createTempTable() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("CREATE TABLE backup_db.temp_exchange LIKE backup_db.exchange");
            statement.execute("INSERT INTO backup_db.temp_exchange SELECT * FROM backup_db.exchange;");
            connection.commit();
        } catch (SQLException e) {
            DBUtils.rollbackQuietly(connection);
            logger.error(e);
        } finally {
            DBUtils.closeStatementQuietly(statement);
        }
    }

    public void removeTempTable() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(
                    "DROP TABLE IF EXISTS backup_db.temp_exchange"
            );
            connection.commit();
        } catch (SQLException e) {
            DBUtils.rollbackQuietly(connection);
            logger.error(e);
        } finally {
            DBUtils.closeStatementQuietly(statement);
        }
    }
}
