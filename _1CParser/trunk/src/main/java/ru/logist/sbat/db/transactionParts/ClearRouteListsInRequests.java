package ru.logist.sbat.db.transactionParts;

import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.jsonToBean.beans.RouteListsData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ClearRouteListsInRequests extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private List<RouteListsData> updateRouteLists;

    public ClearRouteListsInRequests(List<RouteListsData> updateRouteLists) {
        this.updateRouteLists = updateRouteLists;
    }

    @Override
    public Statement executePart() throws SQLException {
        if (updateRouteLists.isEmpty())
            return null;
        logger.info("START UPDATE requests clear routeLists");

        PreparedStatement requestUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE requests SET " +
                        "routeListID = NULL," +
                        "warehousePointID = NULL" +
                        "  WHERE requests.routeListID = ?;"
        );

        BidiMap<String, Integer> allRouteListsAsKeyPairs = Selects.getInstance().allRouteListsAsKeyPairs();

        for (RouteListsData updateRouteList : updateRouteLists) {
            try {
                Integer routeListId = getRouteListId(allRouteListsAsKeyPairs, updateRouteList.getRouteListIdExternal());
                requestUpdatePreparedStatement.setInt(1, routeListId);
            } catch (DBCohesionException e) {
                logger.warn(e);
                continue;
            }
            requestUpdatePreparedStatement.addBatch();
        }

        int[] requestsAffectedRecords = requestUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE requests clear routeLists completed, affected records size = [{}]", requestsAffectedRecords.length);

        return requestUpdatePreparedStatement;
    }

    private Integer getRouteListId(BidiMap<String, Integer> allRouteListsAsKeyPairs, String routeListIdExternal) throws DBCohesionException {
        Integer routeListId = allRouteListsAsKeyPairs.get(routeListIdExternal);

        if (routeListId == null)
            throw new DBCohesionException(this.getClass().getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID_EXTERNAL, RouteListsData.FN_ROUTE_LIST_ID_EXTERNAL, routeListIdExternal, "route_lists");
        return routeListId;
    }
}
