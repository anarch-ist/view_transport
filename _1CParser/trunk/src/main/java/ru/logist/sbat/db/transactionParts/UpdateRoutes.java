package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.db.Utils;
import ru.logist.sbat.jsonParser.beans.DirectionsData;
import ru.logist.sbat.jsonParser.beans.RouteListsData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateRoutes extends TransactionPart{
    private List<DirectionsData> updateRoutesArray;
    private List<RouteListsData> updateRouteLists;

    public UpdateRoutes(List<DirectionsData> updateRoutesArray, List<RouteListsData> updateRouteLists) {
        this.updateRoutesArray = updateRoutesArray;
        this.updateRouteLists = updateRouteLists;
    }

    @Override
    PreparedStatement executePart() throws SQLException {
        logger.info("-----------------START update routes table from JSON object:[updateDirections]-----------------");
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO routes (directionIDExternal, dataSourceID, routeName) VALUE (?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  routeName = VALUES(routeName);"
        );
        BidiMap<String, String> allRoutes = selectAllRoutes();


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


        Map<String, String> allPoints = selectAllPoints();

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

    /**
     *
     * @return All pointIDExternals and pointNames from dataBase as map.
     * @throws SQLException
     */
    private Map<String, String> selectAllPoints() throws SQLException {
        Map<String, String> allPoints = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT pointIDExternal, pointName FROM points;");
        while (resultSet.next()) {
            allPoints.put(resultSet.getString(1), resultSet.getString(2));
        }
        return allPoints;
    }

    /**
     *
     * @return All directionIDExternal and routeNames from dataBase as bidimap.
     * @throws SQLException
     */
    private BidiMap<String, String> selectAllRoutes() throws SQLException {
        //
        BidiMap<String, String> allRoutes = new DualHashBidiMap<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT directionIDExternal, routeName FROM routes;");
        while (resultSet.next()) {
            allRoutes.put(resultSet.getString(1), resultSet.getString(2));
        }
        return allRoutes;
    }
}
