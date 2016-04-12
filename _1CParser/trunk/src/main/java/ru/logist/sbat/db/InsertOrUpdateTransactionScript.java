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
    private static final String OK_STATUS = "OK";
    private static final String ERROR_STATUS = "ERROR";
    public static final String LOGIST_1C = "LOGIST_1C";
    private static final Logger logger = LogManager.getLogger();
    private final Connection connection;
    private final DataFrom1c dataFrom1c;

    public InsertOrUpdateTransactionScript(Connection connection, DataFrom1c dataFrom1c) {

        this.connection = connection;
        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
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
        transactionExecutor.put(2, new UpdatePoints(packageData.getUpdatePoints(), packageData.getUpdateAddresses()));
        transactionExecutor.put(3, new UpdateRoutes(packageData.getUpdateDirections(), packageData.getUpdateRouteLists()));
        transactionExecutor.put(4, new UpdateClients(packageData.getUpdateClients()));
        transactionExecutor.put(5, new UpdateUsers(packageData.getUpdateTraders(), packageData.getUpdateClients()));
        transactionExecutor.put(6, new UpdateRouteListsTable(packageData.getUpdateRouteLists()));
        transactionExecutor.put(7, new UpdateRequests(packageData.getUpdateRequests()));
        transactionExecutor.put(8, new AssignStatusesInRequestsTable(packageData.getUpdateStatuses()));
        transactionExecutor.put(9, new AssignRouteListsInRequestsTable(packageData.getUpdateRouteLists()));

        try {
            transactionExecutor.executeAll();
            connection.commit();
            insertOrUpdateResult.setStatus(OK_STATUS);
            return insertOrUpdateResult;
        } catch(Exception e) {
            e.printStackTrace();
            logger.error(e);
            logger.error("start ROLLBACK");
            DBUtils.rollbackQuietly(connection);
            logger.error("end ROLLBACK");
            insertOrUpdateResult.setStatus(ERROR_STATUS);
            return insertOrUpdateResult;
        } finally {
            transactionExecutor.closeAll();
        }
    }
}
