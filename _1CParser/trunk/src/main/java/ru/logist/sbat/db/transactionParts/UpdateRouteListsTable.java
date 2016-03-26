package ru.logist.sbat.db.transactionParts;


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
        logger.info("-----------------START update routeLists from JSON object:[updateRouteLists]-----------------");

        // create routeLists
        PreparedStatement routeListsInsertPreparedStatement = connection.prepareStatement(
                "INSERT INTO route_lists\n" +
                        "  VALUE (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, getRouteIDByDirectionIDExternal(?, ?))\n" +
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
            String routeListId = updateRouteList.getRouteListId();
            String routeListNumber = updateRouteList.getRouteListNumber();
            Date creationDate = updateRouteList.getRouteListDate();
            Date departureDate = updateRouteList.getDepartureDate();
            String forwarderId = updateRouteList.getForwarderId();
            String driverId = updateRouteList.getDriverId();
            String status = updateRouteList.getStatus();

            String routeIdExternal = null;
            if (updateRouteList.isIntrasiteRoute()) {
                routeIdExternal = updateRouteList.getDirectId();
            } else if (updateRouteList.isTrunkRoute()) {
                routeIdExternal = updateRouteList.getGeneratedRouteId();
            }

            routeListsInsertPreparedStatement.setString(1, routeListId);
            routeListsInsertPreparedStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            routeListsInsertPreparedStatement.setString(3, routeListNumber);
            routeListsInsertPreparedStatement.setDate(4, creationDate);
            routeListsInsertPreparedStatement.setDate(5, departureDate);
            routeListsInsertPreparedStatement.setNull(6, Types.INTEGER); // palletsQty
            routeListsInsertPreparedStatement.setString(7, forwarderId);
            routeListsInsertPreparedStatement.setString(8, driverId);
            routeListsInsertPreparedStatement.setString(9, null); // driverPhoneNumber
            routeListsInsertPreparedStatement.setString(10, null); // license plate
            routeListsInsertPreparedStatement.setString(11, status);
            routeListsInsertPreparedStatement.setString(12, routeIdExternal);
            routeListsInsertPreparedStatement.setString(13, InsertOrUpdateTransactionScript.LOGIST_1C);

            routeListsInsertPreparedStatement.addBatch();
        }

        int[] routeListsAffectedRecords = routeListsInsertPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO route_lists completed, affected records size = [{}]", routeListsAffectedRecords.length);

        return routeListsInsertPreparedStatement;
    }
}
