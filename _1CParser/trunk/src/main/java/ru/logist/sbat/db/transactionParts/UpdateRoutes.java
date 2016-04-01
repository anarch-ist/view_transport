package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.db.Utils;
import ru.logist.sbat.jsonParser.beans.DirectionsData;
import ru.logist.sbat.jsonParser.beans.RouteListsData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class UpdateRoutes extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private List<DirectionsData> updateRoutesArray;
    private List<RouteListsData> updateRouteLists;

    public UpdateRoutes(List<DirectionsData> updateRoutesArray, List<RouteListsData> updateRouteLists) {
        this.updateRoutesArray = updateRoutesArray;
        this.updateRouteLists = updateRouteLists;
    }

    @Override
    public PreparedStatement executePart() throws SQLException, DBCohesionException {
        logger.info("-----------------START update routes table from JSON object:[updateDirections]-----------------");
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO routes (directionIDExternal, dataSourceID, routeName) VALUE (?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  routeName = VALUES(routeName);"
        );
        BidiMap<String, String> allRoutes = Selects.selectAllRoutesAsExtKeyAndName(InsertOrUpdateTransactionScript.LOGIST_1C);

        for (DirectionsData updateRoute : updateRoutesArray) {
            String directionIDExternal = updateRoute.getDirectId();
            String directionName = updateRoute.getDirectName();
            // если у нас новое направление(directionIDExternal нет в БД) и routeName - дублируется => нужно присвоить новый routeName
            String uniqueDirectionName;
            if (!allRoutes.containsKey(directionIDExternal) && allRoutes.containsValue(directionName)) {
                uniqueDirectionName = Utils.getUniqueDirectionName(allRoutes.values(), directionName);
            } else if (allRoutes.containsKey(directionIDExternal) && !allRoutes.containsValue(directionName)) {
                // запрет на переименование, т.к. такое имя уже есть
                logger.warn("directionName [{}] already exist in database ", directionName);
                uniqueDirectionName = directionName;
            }
            else
                uniqueDirectionName = directionName;
            allRoutes.put(directionIDExternal, uniqueDirectionName);

            preparedStatement.setString(1, directionIDExternal);
            preparedStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            preparedStatement.setString(3, uniqueDirectionName);
            preparedStatement.addBatch();
        }


        Map<String, String> allPoints = Selects.selectAllPoints();

        for (RouteListsData updateRouteList : updateRouteLists) {
            if (updateRouteList.isTrunkRoute()) {
                // insert or update route
                String generatedRouteId = updateRouteList.getGeneratedRouteId();

                preparedStatement.setString(1, generatedRouteId);
                preparedStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);

                String pointArrivalId = updateRouteList.getPointArrivalId();
                String pointDepartureId = updateRouteList.getPointDepartureId();

                if (allPoints.get(pointArrivalId) == null)
                    throw new DBCohesionException("Point arrival ID = [" + pointArrivalId + "] is not contained in points table");
                if (allPoints.get(pointDepartureId) == null)
                    throw new DBCohesionException("Point departure ID = [" + pointDepartureId + "] is not contained in points table");

                preparedStatement.setString(3,
                        allPoints.get(pointDepartureId) + "-" + allPoints.get(pointArrivalId)
                );
                preparedStatement.addBatch();
            }
            connection.commit();
        }

        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [routes] completed, affected records size = [{}]", affectedRecords.length);

        return preparedStatement;
    }

}
