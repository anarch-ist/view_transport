package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.jsonToBean.beans.RouteListsData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * this method should be executed only after ClearRouteListsInRequests
 */
public class AssignRouteListsInRequests extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private List<RouteListsData> updateRouteLists;

    public AssignRouteListsInRequests(List<RouteListsData> updateRouteLists) {
        this.updateRouteLists = updateRouteLists;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateRouteLists.isEmpty())
            return null;
        logger.info("START UPDATE requests assign routeLists");
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

            Integer routeListId;
            Integer warehousePointId;
            try {
                routeListId = getRouteListId(allRouteListsAsKeyPairs, updateRouteList.getRouteListIdExternal());
                warehousePointId = getWarehousePointId(allPointsAsKeyPairs, updateRouteList.getPointDepartureId());
            } catch (DBCohesionException e) {
                logger.warn(e);
                continue;
            }

            for(Object requestIDExternalAsObject: requests) {
                String requestIDExternal = (String) requestIDExternalAsObject;
                try {
                    requestUpdatePreparedStatement.setInt(1, routeListId);
                    requestUpdatePreparedStatement.setInt(2, warehousePointId);
                    setRequestId(requestUpdatePreparedStatement, requestIDExternal, allRequestsAsKeyPairs.get(requestIDExternal), 3);
                } catch (DBCohesionException e) {
                    logger.warn(e);
                    continue;
                }
                requestUpdatePreparedStatement.addBatch();
            }
        }

        int[] requestsAffectedRecords = requestUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE requests assign routeLists completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestUpdatePreparedStatement;
    }

    private Integer getRouteListId(BidiMap<String, Integer> allRouteListsAsKeyPairs, String routeListIdExternal) throws DBCohesionException {
        Integer routeListId = allRouteListsAsKeyPairs.get(routeListIdExternal);
        if (routeListId == null) {
            throw new DBCohesionException(this.getClass().getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID_EXTERNAL, RouteListsData.FN_ROUTE_LIST_ID_EXTERNAL, routeListIdExternal, "route_lists");
        }
        return routeListId;
    }

    private Integer getWarehousePointId(BidiMap<String, Integer> allPointsAsKeyPairs, String pointIdExternal) throws DBCohesionException {
        Integer warehousePointId = allPointsAsKeyPairs.get(pointIdExternal);
        if (warehousePointId == null) {
            throw new DBCohesionException(this.getClass().getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID_EXTERNAL, RouteListsData.FN_POINT_DEPARTURE_ID, pointIdExternal, "points");
        }
        return warehousePointId;
    }

    private void setRequestId(PreparedStatement requestUpdatePreparedStatement, String requestIDExternal, Integer requestId, int parameterIndex) throws DBCohesionException, SQLException {
        if (requestId == null) {
            throw new DBCohesionException(this.getClass().getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID_EXTERNAL, RouteListsData.FN_INVOICES, requestIDExternal, "requests");
        } else {
            requestUpdatePreparedStatement.setInt(parameterIndex, requestId);
        }
    }


}
