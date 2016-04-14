package ru.logist.sbat.db.transactionParts;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.jsonParser.beans.StatusData;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class AssignStatusesInRequestsTable extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private List<StatusData> updateStatuses;

    public AssignStatusesInRequestsTable(List<StatusData> updateStatuses) {
        this.updateStatuses = updateStatuses;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateStatuses.isEmpty())
            return null;

        logger.info("-----------------START assign statuses in requests table from JSON object:[updateStatus]-----------------");
        PreparedStatement requestsUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE requests SET boxQty = ?, requestStatusID = ?, commentForStatus = ?, lastStatusUpdated = ?, routeListID = VALUES(routeListID) WHERE requestIDExternal = ? AND dataSourceID = ?;"
        );

        for (StatusData updateStatus : updateStatuses) {
            String requestIdExternal = updateStatus.getRequestId();
            Long numBoxes = updateStatus.getNumBoxes();
            String requestStatus = updateStatus.getStatus();
            Date timeOutStatus = updateStatus.getTimeOutStatus();
            String comment = updateStatus.getComment();
            if (numBoxes == null)
                requestsUpdatePreparedStatement.setNull(1, Types.INTEGER);
            else {
                requestsUpdatePreparedStatement.setLong(1, numBoxes);
            }
            if (requestStatus == null) {
                logger.warn("requestStatus for requestId = [{}] is null, new requestStatus = [CREATED]", requestIdExternal);
                requestStatus = "CREATED";
            }
            requestsUpdatePreparedStatement.setString(2, requestStatus);
            requestsUpdatePreparedStatement.setString(3, comment);
            requestsUpdatePreparedStatement.setDate(4, timeOutStatus);
            requestsUpdatePreparedStatement.setString(5, requestIdExternal);
            requestsUpdatePreparedStatement.setString(6, InsertOrUpdateTransactionScript.LOGIST_1C);
            requestsUpdatePreparedStatement.addBatch();
        }
        int[] requestsAffectedRecords = requestsUpdatePreparedStatement.executeBatch();
        logger.info("ASSIGN statuses in requests completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestsUpdatePreparedStatement;
    }
}
