package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.jsonToBean.beans.StatusData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class AssignStatusesInRequests extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private static final Logger cohesionLogger = LogManager.getLogger("dbCohesion");
    private List<StatusData> updateStatuses;

    public AssignStatusesInRequests(List<StatusData> updateStatuses) {
        this.updateStatuses = updateStatuses;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateStatuses.isEmpty())
            return null;

        BidiMap<String, Integer> allRequestsAsKeyPairs = Selects.getInstance().allRequestsAsKeyPairs();

        logger.info("START assign statuses in requests table from JSON object:[updateStatus]");
        PreparedStatement requestsUpdatePrSt = connection.prepareStatement(
                "UPDATE requests SET boxQty = ?, requestStatusID = ?, commentForStatus = ?, lastStatusUpdated = ? WHERE requestID = ?;"
        );

        for (StatusData updateStatus : updateStatuses) {

            setNumBoxes(requestsUpdatePrSt, 1, updateStatus.getNumBoxes());
            requestsUpdatePrSt.setString(2, updateStatus.getStatus());
            requestsUpdatePrSt.setString(3, updateStatus.getComment());
            requestsUpdatePrSt.setTimestamp(  4, updateStatus.getTimeOutStatus());
            try {
                setRequestId(requestsUpdatePrSt, 5, updateStatus.getRequestId(), allRequestsAsKeyPairs);
            } catch (DBCohesionException e) {
                cohesionLogger.warn(e);
                continue;
            }
            requestsUpdatePrSt.addBatch();
        }
        int[] requestsAffectedRecords = requestsUpdatePrSt.executeBatch();
        logger.info("ASSIGN statuses in requests completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestsUpdatePrSt;
    }

    private void setNumBoxes(PreparedStatement requestsUpdatePrSt, Integer parameterIndex, Long numBoxes) throws SQLException {
        if (numBoxes == null)
            requestsUpdatePrSt.setNull(parameterIndex, Types.INTEGER);
        else {
            requestsUpdatePrSt.setInt(parameterIndex, numBoxes.intValue());
        }
    }

    private void setRequestId(PreparedStatement requestsUpdatePrSt, Integer parameterIndex, String requestIdExternal, BidiMap<String, Integer> allRequestsAsKeyPairs) throws SQLException, DBCohesionException {
        Integer requestId = allRequestsAsKeyPairs.get(requestIdExternal);
        if (requestId == null) {
            throw new DBCohesionException(this.getClass().getSimpleName(), StatusData.FN_REQUEST_ID, StatusData.FN_REQUEST_ID, requestIdExternal, "requests");
        } else {
            requestsUpdatePrSt.setInt(parameterIndex, requestId);
        }
    }
}
