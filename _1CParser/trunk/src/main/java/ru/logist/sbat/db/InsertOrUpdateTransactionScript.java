package ru.logist.sbat.db;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.beans.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * В случае, если ID совпали, с уже существующим - то делается UPDATE, если же такого ID нет, то делается INSERT
 */
public class InsertOrUpdateTransactionScript {
    private static final String OK_STATUS = "OK";
    private static final String ERROR_STATUS = "ERROR";
    public static final String LOGIST_1C = "LOGIST_1C";
    private static final Logger logger = LogManager.getLogger();
    private final Connection connection;
    private final DataFrom1c dataFrom1c;
    private InsertOrUpdateResult insertOrUpdateResult;
    
    public InsertOrUpdateTransactionScript(Connection connection, DataFrom1c dataFrom1c) {
        this.connection = connection;
        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.dataFrom1c = dataFrom1c;
    }

    /**
     * this method may run only after updateData
     * @return InsertOrUpdateResult
     */
    public InsertOrUpdateResult getResult() {
        if (insertOrUpdateResult == null)
            throw new IllegalStateException("you must start method updateData() before launch this method");
        return insertOrUpdateResult;
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

    private Set<String> selectAllClientsIdExternal() throws SQLException {
        Set<String> result = new HashSet<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT clientIDExternal FROM clients;");
        while (resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        return result;
    }


    public void updateData() {
        String server = dataFrom1c.getServer();
        logger.info(Util.getParameterizedString("server = {}", server));
        Integer packageNumber = dataFrom1c.getPackageNumber().intValue();
        logger.info(Util.getParameterizedString("packageNumber = {}", packageNumber));
        logger.info(Util.getParameterizedString("dateCreated = {}", dataFrom1c.getCreated()));
        insertOrUpdateResult = new InsertOrUpdateResult();
        insertOrUpdateResult.setServer(server);
        insertOrUpdateResult.setPackageNumber(packageNumber);

        PreparedStatement
                updateExchangePrepStatement = null,
                updatePointsPrepStatement = null,
                updateRoutesPrepStatement = null,
                updateRequestsPrepStatement = null,
                updateRequestsStatusesPrepStatement = null,
                newRoutesFromRouteListsStm = null,
                updateMarketAgentUsersPrepStatement = null;

        PreparedStatement[] routeListsInsertPreparedStatements = null;
        PreparedStatement[] updateClientsPreparedStatements = null;

        try {
            PackageData packageData = dataFrom1c.getPackageData();
            updateExchangePrepStatement = batchPackageData(dataFrom1c);
            updatePointsPrepStatement = batchPointsData(packageData.getUpdatePoints(), packageData.getUpdateAddresses());
            updateRoutesPrepStatement = batchRoutesData(packageData.getUpdateDirections());
            updateMarketAgentUsersPrepStatement = batchMarketAgentUser(packageData.getUpdateTraders());
            updateClientsPreparedStatements = batchClients(packageData.getUpdateClients());
            updateRequestsPrepStatement = batchRequests(packageData.getUpdateRequests());
            updateRequestsStatusesPrepStatement = batchRequestStatuses(packageData.getUpdateStatuses());
            newRoutesFromRouteListsStm = batchNewRoutesFromRouteLists(packageData.getUpdateRouteLists());
            routeListsInsertPreparedStatements = batchRouteLists(packageData.getUpdateRouteLists());
            connection.commit();
            insertOrUpdateResult.setStatus(OK_STATUS);
        } catch(Exception e) {
            e.printStackTrace();
            logger.error(e);
            logger.error("start ROLLBACK");
            DBUtils.rollbackQuietly(connection);
            logger.error("end ROLLBACK");
            insertOrUpdateResult.setStatus(ERROR_STATUS);
        } finally {
            DBUtils.closeStatementQuietly(updateExchangePrepStatement);
            DBUtils.closeStatementQuietly(updatePointsPrepStatement);
            DBUtils.closeStatementQuietly(updateRoutesPrepStatement);
            DBUtils.closeStatementQuietly(updateMarketAgentUsersPrepStatement);
            DBUtils.closeStatementQuietly(updateClientsPreparedStatements != null ? updateClientsPreparedStatements[0] : null);
            DBUtils.closeStatementQuietly(updateClientsPreparedStatements != null ? updateClientsPreparedStatements[1] : null);
            DBUtils.closeStatementQuietly(updateRequestsPrepStatement);
            DBUtils.closeStatementQuietly(updateRequestsStatusesPrepStatement);
            DBUtils.closeStatementQuietly(newRoutesFromRouteListsStm);
            DBUtils.closeStatementQuietly(routeListsInsertPreparedStatements != null ? routeListsInsertPreparedStatements[0] : null);
            DBUtils.closeStatementQuietly(routeListsInsertPreparedStatements != null ? routeListsInsertPreparedStatements[1] : null);

        }
    }

    private PreparedStatement batchPackageData(DataFrom1c dataFrom1C) throws SQLException {
        logger.info("-----------------START update exchange table from JSON object:[dataFrom1C]-----------------");
        PreparedStatement preparedStatement =  connection.prepareStatement(
                "INSERT INTO exchange (packageNumber, serverName, dataSource, packageCreated, packageData)\n" +
                        "VALUE (?, ?, ?, ?, ?);"
        );
        preparedStatement.setInt(1, dataFrom1C.getPackageNumber().intValue());
        preparedStatement.setString(2, dataFrom1C.getServer());
        preparedStatement.setString(3, LOGIST_1C);
        preparedStatement.setDate(4, dataFrom1C.getCreated());
        preparedStatement.setString(5, dataFrom1C.getRawJsonObject());
        preparedStatement.executeUpdate();
        logger.info("INSERT INTO exchange table completed packageNumber = [{}]", dataFrom1C.getPackageNumber().intValue());
        return preparedStatement;
    }

    private PreparedStatement batchPointsData(List<PointData> updatePointsArray, List<AddressData> updateAddresses) throws SQLException {
        logger.info("-----------------START update points table from JSON objects:[updatePointsArray], [updateAddresses]-----------------");
        PreparedStatement pointsStatement =  connection.prepareStatement(
                "INSERT INTO points (pointIDExternal, dataSourceID, pointName, address, email, pointTypeID, responsiblePersonId)\n" +
                        "VALUE (?, ?, ?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  pointName           = VALUES(pointName),\n" +
                        "  address             = VALUES(address),\n" +
                        "  email               = VALUES(email),\n" +
                        "  pointTypeID         = VALUES(pointTypeID),\n" +
                        "  responsiblePersonId = VALUES(responsiblePersonId);"
        );

        // create or update points from updatePoints JSON array
        for (PointData updatePoint : updatePointsArray) {
            pointsStatement.setString(1, updatePoint.getPointId());
            pointsStatement.setString(2, LOGIST_1C);
            pointsStatement.setString(3, updatePoint.getPointName());
            pointsStatement.setString(4, updatePoint.getPointAddress());
            pointsStatement.setString(5, updatePoint.getPointEmails());
            pointsStatement.setString(6, updatePoint.getPointType());
            pointsStatement.setString(7, updatePoint.getResponsiblePersonId());
            pointsStatement.addBatch();
        }

        // create or update points from updateAddresses JSON array
        // add only unique points
        Utils.UniqueCheck uniqueCheckAddressId = Utils.getUniqueCheckObject("addressId");
        for (AddressData updateAddress : updateAddresses) {
            String pointIdExternal = updateAddress.getAddressId();
            if (!uniqueCheckAddressId.isUnique(pointIdExternal))
                continue;

            String pointName = updateAddress.getAddressShot();
            String pointAddress = updateAddress.getAddressFull();

            pointsStatement.setString(1, pointIdExternal);
            pointsStatement.setString(2, LOGIST_1C);
            pointsStatement.setString(3, pointName);
            pointsStatement.setString(4, pointAddress);
            pointsStatement.setString(5, "");
            pointsStatement.setString(6, "AGENCY");
            pointsStatement.setString(7, "");
            pointsStatement.addBatch();
        }

        int[] affectedRecords = pointsStatement.executeBatch();
        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [points] completed, affected records size = [{}]", affectedRecords.length);
        return pointsStatement;
    }

    private PreparedStatement batchRoutesData(List<DirectionsData> updateRoutesArray) throws SQLException {
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
            preparedStatement.setString(2, LOGIST_1C);
            preparedStatement.setString(3, uniqueDirectionName);
            preparedStatement.addBatch();
        }
        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [routes] completed, affected records size = [{}]", affectedRecords.length);
        return preparedStatement;
    }


    private PreparedStatement getUsersTablePrepStm() throws SQLException {
        return connection.prepareStatement(
                "INSERT INTO users (login, userName, phoneNumber, email, position, userRoleID)\n" +
                        "  VALUE (?, ?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  userName    = VALUES(userName),\n" +
                        "  phoneNumber = VALUES(phoneNumber),\n" +
                        "  email       = VALUES(email),\n" +
                        "  position    = VALUES(position),\n" +
                        "  userRoleID  = VALUES(userRoleID);\n"
        );
    }


    private static final String MARKET_AGENT = "MARKET_AGENT";
    private PreparedStatement batchMarketAgentUser(List<TraderData> updateMarketAgents) throws SQLException {
        logger.info("-----------------START update users table from JSON object:[updateTrader]-----------------");
        PreparedStatement result = getUsersTablePrepStm();
        // only insert or update data without passwords
        Utils.UniqueCheck uniqueCheckLogins = Utils.getUniqueCheckObject("traderId");
        // check trader id is unique
        for (TraderData updateUser : updateMarketAgents) {
            String login = updateUser.getTraderId();
            if (!uniqueCheckLogins.isUnique(login))
                continue;
            result.setString(1, login);
            result.setString(2, updateUser.getTraderName());
            result.setString(3, updateUser.getTraderPhone());
            result.setString(4, updateUser.getTraderEmails());
            result.setString(5, updateUser.getTraderOffice());
            result.setString(6, MARKET_AGENT);
            result.addBatch();
        }
        int[] insertedOrUpdateRecords = result.executeBatch();
        logger.info("INSERT OR UPDATE INTO users completed, affected records size = [{}]", insertedOrUpdateRecords.length);
        return result;
    }

    private static final String CLIENT_MANAGER = "CLIENT_MANAGER";
    private PreparedStatement[] batchClients(List<ClientData> updateClients) throws SQLException {
        logger.info("-----------------START update clients table from JSON object:[clients]-----------------");
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN)\n" +
                        "  VALUE (?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  clientName = VALUES(clientName),\n" +
                        "  INN        = VALUES(INN);"
        );

        PreparedStatement usersPrepStatement = getUsersTablePrepStm();

        Utils.UniqueCheck uniqueCheckClientId = Utils.getUniqueCheckObject("clientId");
        for (ClientData updateClient : updateClients) {
            String clientIDExternal = updateClient.getClientId();
            if (!uniqueCheckClientId.isUnique(clientIDExternal))
                continue;

            String clientName = updateClient.getClientName();
            String inn = updateClient.getClientINN();
            preparedStatement.setString(1, clientIDExternal);
            preparedStatement.setString(2, LOGIST_1C);
            preparedStatement.setString(3, clientName);
            preparedStatement.setString(4, inn);
            preparedStatement.addBatch();
            // TODO FIXME
            usersPrepStatement.setString(1, clientIDExternal + "-client");
            usersPrepStatement.setString(2, clientName);
            usersPrepStatement.setString(3, null);
            usersPrepStatement.setString(4, null);
            usersPrepStatement.setString(5, null);
            usersPrepStatement.setString(6, CLIENT_MANAGER);
            usersPrepStatement.addBatch();
        }
        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE for clients completed, affected records size = [{}]", affectedRecords.length);

        int[] insertedOrUpdateRecords = usersPrepStatement.executeBatch();
        logger.info("INSERT OR UPDATE for users completed, affected records size = [{}]", insertedOrUpdateRecords.length);

        return new PreparedStatement[]{preparedStatement, usersPrepStatement};
    }

    private PreparedStatement batchRequests(List<RequestsData> updateRequests) throws SQLException {
        logger.info("-----------------START update requests table from JSON object:[updateRequests]-----------------");
        Map<String, String> allPoints = selectAllPoints();
        Set<String> allClientsIdExternal = selectAllClientsIdExternal();

        PreparedStatement requestsPreparedStatement = connection.prepareStatement(
            "INSERT INTO requests\n" +
                    "  VALUE\n" +
                    "  (NULL,\n" +
                    "    ?, ?, ?, ?,\n" +
                    "    (SELECT clients.clientID FROM clients WHERE clients.clientIDExternal = ? AND clients.dataSourceID = ?),\n" +
                    "    (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?),\n" +
                    "    (SELECT users.userID FROM users WHERE users.login = ?),\n" +
                    "    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,\n" +
                    "   NOW(),\n" +
                    "   getUserIDByLogin('parser'),\n" +
                    "   'CREATED',\n" +
                    "   ?,\n" +
                    "   NULL,\n" +
                    "   NULL,\n" +
                    "   NULL\n" +
                    "  )\n" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "  requestNumber           = VALUES(requestNumber),\n" +
                    "  requestDate             = VALUES(requestDate),\n" +
                    "  clientID                = VALUES(clientID),\n" +
                    "  destinationPointID      = VALUES(destinationPointID),\n" +
                    "  marketAgentUserID       = VALUES(marketAgentUserID),\n" +
                    "  invoiceNumber           = VALUES(invoiceNumber),\n" +
                    "  invoiceDate             = VALUES(invoiceDate),\n" +
                    "  documentNumber          = VALUES(documentNumber),\n" +
                    "  documentDate            = VALUES(documentDate),\n" +
                    "  firma                   = VALUES(firma),\n" +
                    "  storage                 = VALUES(storage),\n" +
                    "  contactName             = VALUES(contactName),\n" +
                    "  contactPhone            = VALUES(contactPhone),\n" +
                    "  deliveryOption          = VALUES(deliveryOption),\n" +
                    "  deliveryDate            = VALUES(deliveryDate),\n" +
                    "  boxQty                  = VALUES(boxQty),\n" +
                    "  weight                  = VALUES(weight),\n" +
                    "  volume                  = VALUES(volume),\n" +
                    "  goodsCost               = VALUES(goodsCost),\n" +
                    "  lastStatusUpdated       = VALUES(lastStatusUpdated),\n" +
                    "  lastModifiedBy          = VALUES(lastModifiedBy),\n" +
                    "  requestStatusID         = VALUES(requestStatusID),\n" +
                    "  commentForStatus        = VALUES(commentForStatus),\n" +
                    "  warehousePointID        = VALUES(warehousePointID),\n" +
                    "  routeListID             = VALUES(routeListID),\n" +
                    "  lastVisitedRoutePointID = VALUES(lastVisitedRoutePointID);"
        );

        for (RequestsData updateRequest : updateRequests) {
            requestsPreparedStatement.setString(1, updateRequest.getRequestId());
            requestsPreparedStatement.setString(2, LOGIST_1C);
            requestsPreparedStatement.setString(3, updateRequest.getRequestNumber());
            requestsPreparedStatement.setDate(4,  updateRequest.getRequestDate());
            String clientId = updateRequest.getClientId();
            if (!allClientsIdExternal.contains(clientId)) {
                throw new DBCohesionException(Util.getParameterizedString("requestId {} has clientId = {} that is not contained in clients table.", updateRequest.getRequestId(), clientId));
            } else
                requestsPreparedStatement.setString(5, clientId);
            requestsPreparedStatement.setString(6, LOGIST_1C);
            String addressId = updateRequest.getAddressId();
            if (!allPoints.containsKey(addressId)) {
                logger.warn("requestId [{}] has addressId = [{}] that is not contained in points table. Set NULL for pointID", updateRequest.getRequestId(), addressId);
                requestsPreparedStatement.setString(7, null);
            }
            else
                requestsPreparedStatement.setString(7, addressId);
            requestsPreparedStatement.setString(8, LOGIST_1C);
            requestsPreparedStatement.setString(9, updateRequest.getTraderId());
            requestsPreparedStatement.setString(10, updateRequest.getInvoiceNumber());
            requestsPreparedStatement.setDate(11, updateRequest.getInvoiceDate());
            requestsPreparedStatement.setString(12, updateRequest.getDocumentNumber());
            requestsPreparedStatement.setDate(13, updateRequest.getDocumentDate());
            requestsPreparedStatement.setString(14, updateRequest.getFirma());
            requestsPreparedStatement.setString(15, updateRequest.getStorage());
            requestsPreparedStatement.setString(16, updateRequest.getContactName());
            requestsPreparedStatement.setString(17, updateRequest.getContactPhone());
            requestsPreparedStatement.setString(18, updateRequest.getDeliveryOption());
            requestsPreparedStatement.setDate(19, updateRequest.getDeliveryDate());
            requestsPreparedStatement.setNull(20, Types.INTEGER); //boxQty
            requestsPreparedStatement.setNull(21, Types.INTEGER); //weight
            requestsPreparedStatement.setNull(22, Types.INTEGER); //volume
            requestsPreparedStatement.setBigDecimal(23, null); //goodsCost
            requestsPreparedStatement.setString(24, "заявка создана"); //commentForStatus
            requestsPreparedStatement.addBatch();
        }
        int[] requestsAffectedRecords = requestsPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO requests completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestsPreparedStatement;
    }



    private PreparedStatement batchRequestStatuses(List<StatusData> updateStatuses) throws SQLException {
        logger.info("-----------------START update requests table from JSON object:[updateStatus]-----------------");
        PreparedStatement requestsUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE requests SET boxQty = ?, requestStatusID = ?, commentForStatus = ?, lastStatusUpdated = ? WHERE requestIDExternal = ? AND dataSourceID = ?;"
        );

        for (StatusData updateStatus : updateStatuses) {
            String requestIdExternal = updateStatus.getRequestId();
            Long numBoxes = updateStatus.getNumBoxes();
            String requestStatus = updateStatus.getStatus();
            Date timeOutStatus = updateStatus.getTimeOutStatus();
            String comment = updateStatus.getComment();
            if (numBoxes == null)
                requestsUpdatePreparedStatement.setNull(1, Types.INTEGER);
            else {
                requestsUpdatePreparedStatement.setLong(1, numBoxes);
            }
            if (requestStatus == null) {
                logger.warn("requestStatus for requestId = [{}] is null, new requestStatus = [CREATED]", requestIdExternal);
                requestStatus = "CREATED";
            }
            requestsUpdatePreparedStatement.setString(2, requestStatus);
            requestsUpdatePreparedStatement.setString(3, comment);
            requestsUpdatePreparedStatement.setDate(4, timeOutStatus);
            requestsUpdatePreparedStatement.setString(5, requestIdExternal);
            requestsUpdatePreparedStatement.setString(6, LOGIST_1C);

            requestsUpdatePreparedStatement.addBatch();
        }
        int[] requestsAffectedRecords = requestsUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE requests completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestsUpdatePreparedStatement;
    }

    private PreparedStatement batchNewRoutesFromRouteLists(List<RouteListsData> updateRouteLists) throws SQLException {
        PreparedStatement insertOrUpdateRoutesPreparedStatement = connection.prepareStatement(
                "INSERT INTO routes (directionIDExternal, dataSourceID, routeName) VALUE (?,?,?) " +
                        "ON DUPLICATE KEY UPDATE routeName = VALUES(routeName);"
        );
        Map<String, String> allPoints = selectAllPoints();

        for (RouteListsData updateRouteList : updateRouteLists) {
            if (updateRouteList.isTrunkRoute()) {
                // insert or update route
                String generatedRouteId = updateRouteList.getGeneratedRouteId();

                insertOrUpdateRoutesPreparedStatement.setString(1, generatedRouteId);
                insertOrUpdateRoutesPreparedStatement.setString(2, LOGIST_1C);

                String pointArrivalId = updateRouteList.getPointArrivalId();
                String pointDepartureId = updateRouteList.getPointDepartureId();

                if (allPoints.get(pointArrivalId) == null)
                    throw new DBCohesionException("Point arrival ID = [" + pointArrivalId + "] is not contained in points table");
                if (allPoints.get(pointDepartureId) == null)
                    throw new DBCohesionException("Point departure ID = [" + pointDepartureId + "] is not contained in points table");

                insertOrUpdateRoutesPreparedStatement.setString(3,
                        allPoints.get(pointDepartureId) + "-" + allPoints.get(pointArrivalId)
                );
                insertOrUpdateRoutesPreparedStatement.addBatch();
            }
            connection.commit();
        }

        int[] routesAffectedRecords = insertOrUpdateRoutesPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO routes completed, affected records size = [{}]", routesAffectedRecords.length);
        return insertOrUpdateRoutesPreparedStatement;
    }


    private PreparedStatement[] batchRouteLists(List<RouteListsData> updateRouteLists) throws SQLException {
        logger.info("-----------------START update routeLists and requests table from JSON object:[updateRouteLists]-----------------");

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

        // update requests data
        PreparedStatement requestUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE requests SET " +
                        "routeListID = (SELECT route_lists.routeListID FROM route_lists WHERE route_lists.routeListIDExternal = ? AND route_lists.dataSourceID = ?)," +
                        "warehousePointID = (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?)" +
                        "  WHERE requests.requestIDExternal = ? AND requests.dataSourceID = ?;"

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
            String pointDepartureId = updateRouteList.getPointDepartureId(); // pointIDExternal
            Set<String> requests = updateRouteList.getRequests(); // list of requests inside routeList

            String routeIdExternal = null;
            if (updateRouteList.isIntrasiteRoute()) {
                routeIdExternal = updateRouteList.getDirectId();
            } else if (updateRouteList.isTrunkRoute()) {
                routeIdExternal = updateRouteList.getGeneratedRouteId();
            }

            routeListsInsertPreparedStatement.setString(1, routeListId);
            routeListsInsertPreparedStatement.setString(2, LOGIST_1C);
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
            routeListsInsertPreparedStatement.setString(13, LOGIST_1C);

            routeListsInsertPreparedStatement.addBatch();

            for(Object requestIDExternalAsObject: requests) {
                String requestIDExternal = (String) requestIDExternalAsObject;
                requestUpdatePreparedStatement.setString(1, routeListId);
                requestUpdatePreparedStatement.setString(2, LOGIST_1C);
                requestUpdatePreparedStatement.setString(3, pointDepartureId);
                requestUpdatePreparedStatement.setString(4, LOGIST_1C);
                requestUpdatePreparedStatement.setString(5, requestIDExternal);
                requestUpdatePreparedStatement.setString(6, LOGIST_1C);
                requestUpdatePreparedStatement.addBatch();
            }

        }

        int[] routeListsAffectedRecords = routeListsInsertPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO route_lists completed, affected records size = [{}]", routeListsAffectedRecords.length);

        int[] requestsAffectedRecords = requestUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE routeListID and warehousePoint for requests completed, affected records size = [{}]", requestsAffectedRecords.length);

        return new PreparedStatement[]{routeListsInsertPreparedStatement, requestUpdatePreparedStatement};
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

}
