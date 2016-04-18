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

        BidiMap<String, Integer> allRequestsAsKeyPairs = Selects.getInstance().allRequestsAsKeyPairs();
        BidiMap<String, Integer> allPointsAsKeyPairs = Selects.getInstance().allPointsAsKeyPairs();
        BidiMap<String, Integer> allRouteListsAsKeyPairs = Selects.getInstance().allRouteListsAsKeyPairs();

        PreparedStatement requestUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE requests SET " +
                        "routeListID = ?," +
                        "warehousePointID = ?" +
                        "  WHERE requests.requestID = ?;"
        );

        for (RouteListsData updateRouteList : updateRouteLists) {
            Set<String> requests = updateRouteList.getRequests(); // list of requests inside routeList
            Integer warehousePointId = getWarehousePointId(allPointsAsKeyPairs, updateRouteList);
            Integer routeListId = getRouteListId(allRouteListsAsKeyPairs, updateRouteList);
            for(Object requestIDExternalAsObject: requests) {
                String requestIDExternal = (String) requestIDExternalAsObject;
                setRequestId(requestUpdatePreparedStatement, requestIDExternal, allRequestsAsKeyPairs.get(requestIDExternal), 1);
                requestUpdatePreparedStatement.setInt(2, warehousePointId);
                requestUpdatePreparedStatement.setInt(3, routeListId);
                requestUpdatePreparedStatement.addBatch();
            }
        }

        int[] requestsAffectedRecords = requestUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE requests assign routeLists completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestUpdatePreparedStatement;
    }

    private Integer getRouteListId(BidiMap<String, Integer> allRouteListsAsKeyPairs, RouteListsData updateRouteList) throws DBCohesionException {
        String routeListIdExternal = updateRouteList.getRouteListId();
        Integer routeListId = allRouteListsAsKeyPairs.get(routeListIdExternal);
        if (routeListId == null)
            throw new DBCohesionException(this.getClass().getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID, RouteListsData.FN_ROUTE_LIST_ID, routeListIdExternal, "route_lists");
        return routeListId;
    }

    private Integer getWarehousePointId(BidiMap<String, Integer> allPointsAsKeyPairs, RouteListsData updateRouteList) throws DBCohesionException {
        String pointIdExternal = updateRouteList.getPointDepartureId();
        Integer warehousePointId = allPointsAsKeyPairs.get(pointIdExternal);
        if (warehousePointId == null) {
            throw new DBCohesionException(this.getClass().getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID, RouteListsData.FN_POINT_DEPARTURE_ID, pointIdExternal, "points");
        }
        return warehousePointId;
    }

    private void setRequestId(PreparedStatement requestUpdatePreparedStatement, String requestIDExternal, Integer requestId, int parameterIndex) throws DBCohesionException, SQLException {
        if (requestId == null) {
            throw new DBCohesionException(this.getClass().getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID, RouteListsData.FN_INVOICES, requestIDExternal, "requests");
        } else {
            requestUpdatePreparedStatement.setInt(parameterIndex, requestId);
        }
    }
}
