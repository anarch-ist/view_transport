package ru.logist.sbat.db.transactionParts;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.jsonParser.beans.RouteListsData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class AssignRouteListsInRequestsTable extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private List<RouteListsData> updateRouteLists;

    public AssignRouteListsInRequestsTable(List<RouteListsData> updateRouteLists) {
        this.updateRouteLists = updateRouteLists;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateRouteLists.isEmpty())
            return null;
        PreparedStatement requestUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE requests SET " +
                        "routeListID = (SELECT route_lists.routeListID FROM route_lists WHERE route_lists.routeListIDExternal = ? AND route_lists.dataSourceID = ?)," +
                        "warehousePointID = (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?)" +
                        "  WHERE requests.requestIDExternal = ? AND requests.dataSourceID = ?;"

        );

        for (RouteListsData updateRouteList : updateRouteLists) {
            Set<String> requests = updateRouteList.getRequests(); // list of requests inside routeList
            for(Object requestIDExternalAsObject: requests) {
                String requestIDExternal = (String) requestIDExternalAsObject;
                requestUpdatePreparedStatement.setString(1, updateRouteList.getRouteListId());
                requestUpdatePreparedStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
                requestUpdatePreparedStatement.setString(3, updateRouteList.getPointDepartureId()); // pointIDExternal
                requestUpdatePreparedStatement.setString(4, InsertOrUpdateTransactionScript.LOGIST_1C);
                requestUpdatePreparedStatement.setString(5, requestIDExternal);
                requestUpdatePreparedStatement.setString(6, InsertOrUpdateTransactionScript.LOGIST_1C);
                requestUpdatePreparedStatement.addBatch();
            }

        }

        int[] requestsAffectedRecords = requestUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE requests assign routeLists completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestUpdatePreparedStatement;
    }
}
