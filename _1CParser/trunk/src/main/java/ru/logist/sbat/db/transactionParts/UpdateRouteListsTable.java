package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.jsonParser.beans.RouteListsData;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class UpdateRouteListsTable extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private List<RouteListsData> updateRouteLists;

    public UpdateRouteListsTable(List<RouteListsData> updateRouteLists) {
        this.updateRouteLists = updateRouteLists;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateRouteLists.isEmpty())
            return null;
        logger.info("-----------------START update routeLists from JSON object:[updateRouteLists]-----------------");
        BidiMap<String, Integer> allUsersAsKeyPairs = Selects.selectAllUsersAsKeyPairs(InsertOrUpdateTransactionScript.LOGIST_1C);
        BidiMap<String, Integer> allRoutesAsKeyPairs = Selects.selectAllRoutesAsKeyPairs(InsertOrUpdateTransactionScript.LOGIST_1C);
        // create routeLists
        PreparedStatement routeListsInsertPreparedStatement = connection.prepareStatement(
                "INSERT INTO route_lists\n" +
                        "  VALUE (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  routeListNumber   = VALUES(routeListNumber),\n" +
                        "  creationDate      = VALUES(creationDate),\n" +
                        "  departureDate     = VALUES(departureDate),\n" +
                        "  palletsQty        = VALUES(palletsQty),\n" +
                        "  forwarderId       = VALUES(forwarderId),\n" +
                        "  driverId          = VALUES(driverId),\n" +
                        "  driverPhoneNumber = VALUES(driverPhoneNumber),\n" +
                        "  licensePlate      = VALUES(licensePlate),\n" +
                        "  status            = VALUES(status),\n" +
                        "  routeID           = VALUES(routeID);"
        );
        for (RouteListsData updateRouteList : updateRouteLists) {

            //common values
            String driverId = updateRouteList.getDriverId();
            String routeIdExternal = null;
            if (updateRouteList.isIntrasiteRoute()) {
                routeIdExternal = updateRouteList.getDirectId();
            } else if (updateRouteList.isTrunkRoute()) {
                routeIdExternal = updateRouteList.getGeneratedRouteId();
            }

            if (routeIdExternal == null)
                throw new NullPointerException();

            routeListsInsertPreparedStatement.setString(1, updateRouteList.getRouteListId());
            routeListsInsertPreparedStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            routeListsInsertPreparedStatement.setString(3, updateRouteList.getRouteListNumber());
            routeListsInsertPreparedStatement.setDate(4, updateRouteList.getRouteListDate()); // creationDate
            routeListsInsertPreparedStatement.setDate(5, updateRouteList.getDepartureDate()); // departureDate
            routeListsInsertPreparedStatement.setNull(6, Types.INTEGER); // palletsQty
            routeListsInsertPreparedStatement.setString(7, updateRouteList.getForwarderId());
            if (allUsersAsKeyPairs.containsKey(driverId))
                routeListsInsertPreparedStatement.setInt(8, allUsersAsKeyPairs.get(driverId));
            else
                routeListsInsertPreparedStatement.setNull(8, Types.INTEGER);
            routeListsInsertPreparedStatement.setString(9, null); // driverPhoneNumber
            routeListsInsertPreparedStatement.setString(10, null); // license plate
            routeListsInsertPreparedStatement.setString(11, updateRouteList.getStatus());
            routeListsInsertPreparedStatement.setInt(12, allRoutesAsKeyPairs.get(routeIdExternal));
            routeListsInsertPreparedStatement.addBatch();
        }

        int[] routeListsAffectedRecords = routeListsInsertPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO route_lists completed, affected records size = [{}]", routeListsAffectedRecords.length);

        return routeListsInsertPreparedStatement;
    }
}
