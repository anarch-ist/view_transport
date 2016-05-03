package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.jsonToBean.beans.RouteListsData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class UpdateRouteLists extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private List<RouteListsData> updateRouteLists;

    public UpdateRouteLists(List<RouteListsData> updateRouteLists) {
        this.updateRouteLists = updateRouteLists;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateRouteLists.isEmpty())
            return null;
        logger.info("START update routeLists from JSON object:[updateRouteLists]");
        BidiMap<String, Integer> allUsersAsKeyPairs = Selects.getInstance().allUsersAsKeyPairs();
        BidiMap<String, Integer> allRoutesAsKeyPairs = Selects.getInstance().allRoutesAsKeyPairs();
        // create routeLists
        PreparedStatement routeListsInsertPreparedStatement = connection.prepareStatement(
                "INSERT INTO route_lists(" +
                        " routeListIDExternal," +
                        " dataSourceID," +
                        " routeListNumber," +
                        " creationDate," +
                        " departureDate," +
                        " forwarderId," +
                        " driverID," +
                        " status," +
                        " routeID" +
                        ")\n" +
                        "  VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  routeListNumber   = VALUES(routeListNumber),\n" +
                        "  creationDate      = VALUES(creationDate),\n" +
                        "  departureDate     = VALUES(departureDate),\n" +
                        "  forwarderId       = VALUES(forwarderId),\n" +
                        "  driverId          = VALUES(driverId),\n" +    // foreign key
                        "  status            = VALUES(status),\n" +
                        "  routeID           = VALUES(routeID);"        // foreign key
        );

        for (RouteListsData updateRouteList : updateRouteLists) {

            String routeIdExternal = null;
            if (updateRouteList.isIntrasiteRoute()) {
                routeIdExternal = updateRouteList.getDirectId();
            } else if (updateRouteList.isTrunkRoute()) {
                routeIdExternal = updateRouteList.getGeneratedRouteId();
            }
            if (routeIdExternal == null)
                throw new NullPointerException();

            routeListsInsertPreparedStatement.setString(1, updateRouteList.getRouteListIdExternal());
            routeListsInsertPreparedStatement.setString(2, DBManager.LOGIST_1C);
            routeListsInsertPreparedStatement.setString(3, updateRouteList.getRouteListNumber());
            routeListsInsertPreparedStatement.setDate(4, updateRouteList.getRouteListDate()); // creationDate
            routeListsInsertPreparedStatement.setDate(5, updateRouteList.getDepartureDate()); // departureDate
            routeListsInsertPreparedStatement.setString(6, updateRouteList.getForwarderId());
            try {
                setDriver(allUsersAsKeyPairs, routeListsInsertPreparedStatement, 7, updateRouteList.getDriverId());
                routeListsInsertPreparedStatement.setString(8, updateRouteList.getStatus());
                setRouteId(allRoutesAsKeyPairs, routeListsInsertPreparedStatement, 9, routeIdExternal);
            } catch (DBCohesionException e) {
                logger.warn(e);
                continue;
            }
            routeListsInsertPreparedStatement.addBatch();
        }

        int[] routeListsAffectedRecords = routeListsInsertPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO route_lists completed, affected records size = [{}]", routeListsAffectedRecords.length);

        return routeListsInsertPreparedStatement;
    }


    private void setRouteId(BidiMap<String, Integer> allRoutesAsKeyPairs, PreparedStatement routeListsInsertPreparedStatement, int parameterIndex, String routeIdExternal) throws SQLException, DBCohesionException {
        if (allRoutesAsKeyPairs.containsKey(routeIdExternal))
            routeListsInsertPreparedStatement.setInt(parameterIndex, allRoutesAsKeyPairs.get(routeIdExternal));
        else
            throw new DBCohesionException(UpdateRouteLists.class.getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID_EXTERNAL, RouteListsData.FN_DIRECT_ID, routeIdExternal, "routes");
    }
    private void setDriver(BidiMap<String, Integer> allUsersAsKeyPairs, PreparedStatement routeListsInsertPreparedStatement, int parameterIndex, String driverId) throws SQLException {
        if (allUsersAsKeyPairs.containsKey(driverId))
            routeListsInsertPreparedStatement.setInt(parameterIndex, allUsersAsKeyPairs.get(driverId));
        else
            routeListsInsertPreparedStatement.setNull(parameterIndex, Types.INTEGER);
    }
}
