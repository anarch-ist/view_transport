package ru.logist.sbat.db;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.transactionParts.*;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.beans.*;

import java.sql.*;

/**
 * В случае, если ID совпали, с уже существующим - то делается UPDATE, если же такого ID нет, то делается INSERT
 */
public class InsertOrUpdateTransactionScript {

    public static final String LOGIST_1C = "LOGIST_1C";
    private static final Logger logger = LogManager.getLogger();
    private final Connection connection;
    private final DataFrom1c dataFrom1c;

    public InsertOrUpdateTransactionScript(Connection connection, DataFrom1c dataFrom1c) {

        this.connection = connection;
        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.dataFrom1c = dataFrom1c;
    }

    public InsertOrUpdateResult updateData() {

        String server = dataFrom1c.getServer();
        logger.info(Util.getParameterizedString("server = {}", server));
        Integer packageNumber = dataFrom1c.getPackageNumber().intValue();
        logger.info(Util.getParameterizedString("packageNumber = {}", packageNumber));
        logger.info(Util.getParameterizedString("dateCreated = {}", dataFrom1c.getCreated()));
        InsertOrUpdateResult insertOrUpdateResult = new InsertOrUpdateResult();
        insertOrUpdateResult.setServer(server);
        insertOrUpdateResult.setPackageNumber(packageNumber);

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
            insertOrUpdateResult.setStatus(InsertOrUpdateResult.OK_STATUS);
            return insertOrUpdateResult;
        } catch(Exception e) {
            e.printStackTrace();
            logger.error(e);
            logger.error("start ROLLBACK");
            DBUtils.rollbackQuietly(connection);
            logger.error("end ROLLBACK");
            insertOrUpdateResult.setStatus(InsertOrUpdateResult.ERROR_STATUS);
            return insertOrUpdateResult;
        } finally {
            transactionExecutor.closeAll();
        }
    }
}
