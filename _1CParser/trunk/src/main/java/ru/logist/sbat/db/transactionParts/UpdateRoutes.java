package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
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
        if (updateRoutesArray.isEmpty() && updateRouteLists.isEmpty())
            return null;

        logger.info("START update routes table from JSON object:[updateDirections]");

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO routes (directionIDExternal, dataSourceID, routeName) VALUE (?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  routeName = VALUES(routeName);"
        );

        BidiMap<String, String> allRoutes = Selects.getInstance().selectAllRoutesAsExtKeyAndName(InsertOrUpdateTransactionScript.LOGIST_1C);
        for (DirectionsData updateRoute : updateRoutesArray) {
            String directionIDExternal = updateRoute.getDirectId();
            String directionName = updateRoute.getDirectName();
            String uniqueDirectionName = generateNewDirectionNameIfDuplicate(allRoutes, directionIDExternal, directionName);
            preparedStatement.setString(1, directionIDExternal);
            preparedStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            preparedStatement.setString(3, uniqueDirectionName);
            preparedStatement.addBatch();
        }

        Map<String, Integer> allPoints = Selects.getInstance().allPointsAsKeyPairs();
        Map<Integer, String> allPointNamesById = Selects.getInstance().allPointNamesById(InsertOrUpdateTransactionScript.LOGIST_1C);
        for (RouteListsData updateRouteList : updateRouteLists) {

            // create new route only for trunk routes
            if (updateRouteList.isTrunkRoute()) {
                // insert or update route
                preparedStatement.setString(1, updateRouteList.getGeneratedRouteId());
                preparedStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
                String generatedRouteName = getGeneratedRouteName(allPoints, allPointNamesById, updateRouteList.getPointArrivalId(), updateRouteList.getPointDepartureId());
                preparedStatement.setString(3, generatedRouteName);
                preparedStatement.addBatch();
            }
        }

        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [routes] completed, affected records size = [{}]", affectedRecords.length);

        return preparedStatement;
    }

    private String getGeneratedRouteName(Map<String, Integer> allPoints, Map<Integer, String> allPointNamesById, String pointArrivalIdExternal, String pointDepartureIdExternal) throws DBCohesionException {
        // check points
        Integer pointArrivalId = allPoints.get(pointArrivalIdExternal);
        if (pointArrivalId == null)
            throw new DBCohesionException(UpdateRoutes.class.getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID_EXTERNAL, RouteListsData.FN_POINT_ARRIVAL_ID, pointArrivalIdExternal, "points");
        Integer pointDepartureId = allPoints.get(pointDepartureIdExternal);
        if (pointDepartureId == null)
            throw new DBCohesionException(UpdateRoutes.class.getSimpleName(), RouteListsData.FN_ROUTE_LIST_ID_EXTERNAL, RouteListsData.FN_POINT_DEPARTURE_ID, pointDepartureIdExternal, "points");
        return allPointNamesById.get(pointDepartureId) + "-" + allPointNamesById.get(pointArrivalId);
    }

    private String generateNewDirectionNameIfDuplicate(BidiMap<String, String> allRoutes, String directionIDExternal, String directionName) {
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
        return uniqueDirectionName;
    }

}
