package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.jsonParser.beans.RouteListsData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class AssignRouteListsInRequests extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private List<RouteListsData> updateRouteLists;

    public AssignRouteListsInRequests(List<RouteListsData> updateRouteLists) {
        this.updateRouteLists = updateRouteLists;
    }

    @Override
    public PreparedStatement executePart() throws SQLException, DBCohesionException {
        if (updateRouteLists.isEmpty())
            return null;

        BidiMap<String, Integer> allRequestsAsKeyPairs = Selects.allRequestsAsKeyPairs();
        BidiMap<String, Integer> allPointsAsKeyPairs = Selects.allPointsAsKeyPairs();
        BidiMap<String, Integer> allRouteListsAsKeyPairs = Selects.allRouteListsAsKeyPairs();

        PreparedStatement requestUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE requests SET " +
                        "routeListID = ?," +
                        "warehousePointID = ?" +
                        "  WHERE requests.requestID = ?;"
        );

        for (RouteListsData updateRouteList : updateRouteLists) {
            Set<String> requests = updateRouteList.getRequests(); // list of requests inside routeList

            String pointIdExternal = updateRouteList.getPointDepartureId();
            Integer warehousePointId = allPointsAsKeyPairs.get(pointIdExternal);
            if (warehousePointId == null) {
                throw new DBCohesionException(this.getClass().getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID, RouteListsData.FN_POINT_DEPARTURE_ID, pointIdExternal, "points");
            }

            String routeListIdExternal = updateRouteList.getRouteListId();
            Integer routeListId = allRouteListsAsKeyPairs.get(routeListIdExternal);
            // TODO
            if (routeListId == null) continue;
            for(Object requestIDExternalAsObject: requests) {
                String requestIDExternal = (String) requestIDExternalAsObject;

                // TODO
                try {
                    setRequestId(allRequestsAsKeyPairs, requestUpdatePreparedStatement, requestIDExternal);
                } catch (DBCohesionException e) {
                    continue;
                }


                requestUpdatePreparedStatement.setInt(2, warehousePointId);
                // TODO
                try {
                    setRouteListId(requestUpdatePreparedStatement, routeListId);
                } catch (DBCohesionException e) {
                    continue;
                }


                requestUpdatePreparedStatement.addBatch();
            }

        }

        int[] requestsAffectedRecords = requestUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE requests assign routeLists completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestUpdatePreparedStatement;
    }

    private void setRouteListId(PreparedStatement requestUpdatePreparedStatement, Integer routeListId) throws DBCohesionException, SQLException {
        if (routeListId == null) {
            throw new DBCohesionException("wedwe");
        } else {
            requestUpdatePreparedStatement.setInt(3, routeListId);
        }
    }

    private void setRequestId(BidiMap<String, Integer> allRequestsAsKeyPairs, PreparedStatement requestUpdatePreparedStatement, String requestIDExternal) throws DBCohesionException, SQLException {
        Integer requestId = allRequestsAsKeyPairs.get(requestIDExternal);
        if (requestId == null) {
            throw new DBCohesionException(this.getClass().getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID, RouteListsData.FN_INVOICES, requestIDExternal, "requests");
        } else {
            requestUpdatePreparedStatement.setInt(1, requestId);
        }
    }
}
