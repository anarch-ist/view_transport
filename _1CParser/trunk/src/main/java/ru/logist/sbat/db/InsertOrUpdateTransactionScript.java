package ru.logist.sbat.db;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.jsonParser.beans.*;

import java.sql.*;
import java.util.List;
import java.util.Set;

/**
 * В случае, если ID совпали, с уже существующим - то делается UPDATE, если же такого ID нет, то делается INSERT
 */
public class InsertOrUpdateTransactionScript {

    private static final Logger logger = LogManager.getLogger();
    public static final String LOGIST_1C = "LOGIST_1C";
    private final Connection connection;
    private final DataFrom1c dataFrom1c;
    private InsertOrUpdateResult insertOrUpdateResult;

    public InsertOrUpdateTransactionScript(Connection connection, DataFrom1c dataFrom1c) {
        this.connection = connection;
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

    public void updateData() {
        String server = dataFrom1c.getServer();
        logger.info(server);
        Integer packageNumber = dataFrom1c.getPackageNumber().intValue();
        logger.info(packageNumber);
        logger.info(dataFrom1c.getCreated());
        insertOrUpdateResult = new InsertOrUpdateResult();
        insertOrUpdateResult.setServer(server);
        insertOrUpdateResult.setPackageNumber(packageNumber);

        PreparedStatement
                updateExchangePrepStatement = null,
                updatePointsPrepStatement = null,
                updateRoutesPrepStatement = null,
                updateClientsPrepStatement = null,
                updateRequestsPrepStatement = null,
                updateInvoicesPrepStatement = null,
                updateInvoicesStatusesPrepStatement = null;

        PreparedStatement[] routeListsInsertPreparedStatements = null,
                            updateMarketAgentUsersPrepStatement = null;

        try {
            PackageData packageData = dataFrom1c.getPackageData();
            updateExchangePrepStatement = batchPackageData(dataFrom1c);
            updatePointsPrepStatement = batchPointsData(packageData.getUpdatePoints(), packageData.getUpdateAddresses());
            updateRoutesPrepStatement = batchRoutesData(packageData.getUpdateDirections());
            updateMarketAgentUsersPrepStatement = batchMarketAgentUser(packageData.getUpdateTraders());
            updateClientsPrepStatement = batchClients(packageData.getUpdateClients());
            updateRequestsPrepStatement = batchRequests(packageData.getUpdateRequests());
            updateInvoicesPrepStatement = batchInvoices(packageData.getUpdateRequests());
            updateInvoicesStatusesPrepStatement = batchInvoiceStatuses(packageData.getUpdateStatuses());
            routeListsInsertPreparedStatements = batchRouteLists(packageData.getUpdateRouteLists());
            connection.commit();
            insertOrUpdateResult.setStatus("OK");
        } catch(Exception e) {
            e.printStackTrace();
            logger.error(e);
            logger.error("start ROLLBACK");
            DBUtils.rollbackQuietly(connection);
            logger.error("end ROLLBACK");
            insertOrUpdateResult.setStatus("ERROR");
        } finally {
            DBUtils.closeStatementQuietly(updateExchangePrepStatement);
            DBUtils.closeStatementQuietly(updatePointsPrepStatement);
            DBUtils.closeStatementQuietly(updateRoutesPrepStatement);
            DBUtils.closeStatementQuietly(routeListsInsertPreparedStatements != null ? updateMarketAgentUsersPrepStatement[0] : null);
            DBUtils.closeStatementQuietly(routeListsInsertPreparedStatements != null ? updateMarketAgentUsersPrepStatement[1] : null);
            DBUtils.closeStatementQuietly(updateClientsPrepStatement);
            DBUtils.closeStatementQuietly(updateRequestsPrepStatement);
            DBUtils.closeStatementQuietly(updateInvoicesPrepStatement);
            DBUtils.closeStatementQuietly(updateInvoicesStatusesPrepStatement);
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
        preparedStatement.setString(5, dataFrom1C.getRawData());
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

        // get all routeNames and all routeID from dataBase
        BidiMap<String, String> allRoutes = new DualHashBidiMap<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT directionIDExternal, routeName FROM routes;");
        while (resultSet.next()) {
            allRoutes.put(resultSet.getString(1), resultSet.getString(2));
        }

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

    private static final String userRoleId = "MARKET_AGENT";
    private PreparedStatement[] batchMarketAgentUser(List<TraderData> updateMarketAgents) throws SQLException {
        logger.info("-----------------START update users table from JSON object:[updateTrader]-----------------");
        PreparedStatement insertOrUpdateUsersPrSt = connection.prepareStatement(
                "INSERT INTO users (login, userName, email, phoneNumber, userRoleID)\n" +
                        "  VALUE (?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  userName    = VALUES(userName),\n" +
                        "  email       = VALUES(email),\n" +
                        "  phoneNumber = VALUES(phoneNumber),\n" +
                        "  userRoleID  = VALUES(userRoleID);\n"
        , Statement.RETURN_GENERATED_KEYS);

        // only insert or update data without passwords
        Utils.UniqueCheck uniqueCheckLogins = Utils.getUniqueCheckObject("traderId");
        for (TraderData updateUser : updateMarketAgents) {
            String login = updateUser.getTraderId();
            if (!uniqueCheckLogins.isUnique(login))
                continue;
            String userName = updateUser.getTraderName();
            String email = updateUser.getTraderEmails();
            String phoneNumber = updateUser.getTraderPhone();

            insertOrUpdateUsersPrSt.setString(1, login);
            insertOrUpdateUsersPrSt.setString(2, userName);
            insertOrUpdateUsersPrSt.setString(3, email);
            insertOrUpdateUsersPrSt.setString(4, phoneNumber);
            insertOrUpdateUsersPrSt.setString(5, userRoleId);
            insertOrUpdateUsersPrSt.addBatch();
        }
        int[] insertedOrUpdateRecords = insertOrUpdateUsersPrSt.executeBatch();
        logger.info("INSERT OR UPDATE INTO users completed, affected records size = [{}]", insertedOrUpdateRecords.length);

        // generate and insert new passwords only for new Users, witch was inserted, not updated
        PreparedStatement insertPasswordsPrSt = connection.prepareStatement("UPDATE users SET salt=?, passAndSalt=? WHERE userID = ?;");
        try (ResultSet generatedKeys = insertOrUpdateUsersPrSt.getGeneratedKeys()) {
            while (generatedKeys.next()) {
                long newUserId = generatedKeys.getLong(1);
                String salt = RandomStringUtils.randomAlphanumeric(16);
                String pass = RandomStringUtils.randomAlphanumeric(8);
                String passAndSaltMD5 = Utils.md5(Utils.md5(pass) + salt);
                insertPasswordsPrSt.setString(1, salt);
                insertPasswordsPrSt.setString(2, passAndSaltMD5);
                insertPasswordsPrSt.setLong(3, newUserId);
                logger.info("generated password for new userID [{}] = [{}]", newUserId, pass);
                insertPasswordsPrSt.addBatch();
            }
        }

        int[] insertedRecords = insertPasswordsPrSt.executeBatch();
        logger.info("INSERT passwords for new users completed, affected records size = [{}]", insertedRecords.length);
        return new PreparedStatement[]{insertOrUpdateUsersPrSt, insertPasswordsPrSt};
    }

    private PreparedStatement batchClients(List<ClientData> updateClients) throws SQLException {
        logger.info("-----------------START update clients table from JSON object:[clients]-----------------");
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN)\n" +
                        "  VALUE (?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  clientName = VALUES(clientName),\n" +
                        "  INN        = VALUES(INN);"
        );
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
        }
        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE for clients completed, affected records size = [{}]", affectedRecords.length);
        return preparedStatement;
    }

    private PreparedStatement batchRequests(List<RequestsData> updateRequests) throws SQLException {
        logger.info("-----------------START update requests table from JSON object:[updateRequests]-----------------");
        PreparedStatement requestsPreparedStatement = connection.prepareStatement(
                "INSERT INTO requests\n" +
                        "  SELECT\n" +
                        "    NULL,\n" +
                        "    ?,\n" +
                        "    ?,\n" +
                        "    ?,\n" +
                        "    ?,\n" +
                        "    (SELECT users.userID FROM users WHERE users.login = ?),\n" +
                        "    (SELECT clients.clientID FROM clients WHERE clients.clientIDExternal = ? AND clients.dataSourceID = ?),\n" +
                        "    ?\n" +
                        "  ON DUPLICATE KEY UPDATE\n" +
                        "    requestNumber      = VALUES(requestNumber),\n" +
                        "    creationDate       = VALUES(creationDate),\n" +
                        "    marketAgentUserID  = VALUES(marketAgentUserID),\n" +
                        "    clientID           = VALUES(clientID),\n" +
                        "    destinationPointID = VALUES(destinationPointID);"
        );
        PreparedStatement getPointIdExternal = connection.prepareStatement(
                "SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?;"
        );
        for (RequestsData updateRequest : updateRequests) {

            String requestIDExternal = updateRequest.getRequestId();
            String requestNumber = updateRequest.getRequestNumber();
            String login = updateRequest.getTraderId(); //userID
            String destinationPointIdExternal = updateRequest.getAddressId();
            String clientIdExternal = updateRequest.getClientId();

            requestsPreparedStatement.setString(1, requestIDExternal);
            requestsPreparedStatement.setString(2, LOGIST_1C);
            requestsPreparedStatement.setString(3, requestNumber);
            requestsPreparedStatement.setDate(4, updateRequest.getRequestDate());
            requestsPreparedStatement.setString(5, login);
            requestsPreparedStatement.setString(6, clientIdExternal);
            requestsPreparedStatement.setString(7, LOGIST_1C);

            getPointIdExternal.setString(1, destinationPointIdExternal);
            getPointIdExternal.setString(2, LOGIST_1C);
            int destinationPointId = 0;
            try (ResultSet resultSet = getPointIdExternal.executeQuery()){
                if (resultSet.next()) {
                    destinationPointId = resultSet.getInt(1);
                }
            }
            if (destinationPointId == 0) {
                logger.warn("requestId [{}] has addressId = [{}] that is not contained in points table. Set NULL for pointID", requestIDExternal, destinationPointIdExternal);
                requestsPreparedStatement.setNull(8, Types.INTEGER);
            }
            else
                requestsPreparedStatement.setInt(8, destinationPointId);

            requestsPreparedStatement.addBatch();
        }
        int[] requestsAffectedRecords = requestsPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE on INTO requests completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestsPreparedStatement;
    }

    private PreparedStatement batchInvoices(List<RequestsData> updateRequests) throws SQLException {
        logger.info("-----------------START update invoices table from JSON object:[updateRequests]-----------------");
        PreparedStatement invoicesPreparedStatement = connection.prepareStatement(
            "INSERT INTO invoices\n" +
                    "  VALUE\n" +
                    "  (NULL,\n" +
                    "   ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,\n" +
                    "   NOW(),\n" +
                    "   getUserIDByLogin('parser'),\n" +
                    "   'CREATED',\n" +
                    "   ?,\n" +
                    "   (SELECT requests.requestID FROM requests WHERE requests.requestIDExternal = ? AND requests.dataSourceID = ?),\n" +
                    "   NULL,\n" +
                    "   NULL,\n" +
                    "   NULL\n" +
                    "  ) ON DUPLICATE KEY UPDATE\n" +
                    "  documentNumber          = VALUES(documentNumber),\n" +
                    "  documentDate            = VALUES(documentDate),\n" +
                    "  firma                   = VALUES(firma),\n" +
                    "  contactName             = VALUES(contactName),\n" +
                    "  contactPhone            = VALUES(contactPhone),\n" +
                    "  creationDate            = VALUES(creationDate),\n" +
                    "  deliveryDate            = VALUES(deliveryDate),\n" +
                    "  boxQty                  = VALUES(boxQty),\n" +
                    "  weight                  = VALUES(weight),\n" +
                    "  volume                  = VALUES(volume),\n" +
                    "  goodsCost               = VALUES(goodsCost),\n" +
                    "  storage                 = VALUES(storage),\n" +
                    "  deliveryOption          = VALUES(deliveryOption),\n" +
                    "  lastStatusUpdated       = VALUES(lastStatusUpdated),\n" +
                    "  lastModifiedBy          = VALUES(lastModifiedBy),\n" +
                    "  invoiceStatusID         = VALUES(invoiceStatusID),\n" +
                    "  commentForStatus        = VALUES(commentForStatus),\n" +
                    "  requestID               = VALUES(requestID),\n" +
                    "  warehousePointID        = VALUES(warehousePointID),\n" +
                    "  routeListID             = VALUES(routeListID),\n" +
                    "  lastVisitedRoutePointID = VALUES(lastVisitedRoutePointID);"
        );

        for (RequestsData updateRequest : updateRequests) {
            String requestIDExternal = updateRequest.getRequestId();
            //String invoiceIdExternal = (String)((JSONObject) updateRequest).get("invoiceNumber");
            //TODO temporally use requestId instead invoiceNumber
            String invoiceIdExternal = updateRequest.getRequestId();
            String documentNumber = updateRequest.getDocumentNumber();
            String firma = updateRequest.getFirma();
            String storage = updateRequest.getStorage();
            String contactName = updateRequest.getContactName();
            String contactPhone = updateRequest.getContactPhone();
            String deliveryOption = updateRequest.getDeliveryOption();
            Date creationDate = updateRequest.getInvoiceDate();
            Date documentDate =  updateRequest.getDocumentDate();
            Date deliveryDate =  updateRequest.getDeliveryDate();

            invoicesPreparedStatement.setString(1, invoiceIdExternal);
            invoicesPreparedStatement.setString(2, LOGIST_1C);
            invoicesPreparedStatement.setString(3, documentNumber);
            invoicesPreparedStatement.setDate(4, documentDate);
            invoicesPreparedStatement.setString(5, firma);
            invoicesPreparedStatement.setString(6, contactName);
            invoicesPreparedStatement.setString(7, contactPhone);
            invoicesPreparedStatement.setDate(8, creationDate);
            invoicesPreparedStatement.setDate(9, deliveryDate);
            invoicesPreparedStatement.setNull(10, Types.INTEGER); //boxQty
            invoicesPreparedStatement.setNull(11, Types.INTEGER); //weight
            invoicesPreparedStatement.setNull(12, Types.INTEGER); //volume
            invoicesPreparedStatement.setBigDecimal(13, null); //goodsCost
            invoicesPreparedStatement.setString(14, storage);
            invoicesPreparedStatement.setString(15, deliveryOption);
            invoicesPreparedStatement.setString(16, null); //commentForStatus
            invoicesPreparedStatement.setString(17, requestIDExternal); //requestIdExternal
            invoicesPreparedStatement.setString(18, LOGIST_1C);

            invoicesPreparedStatement.addBatch();
        }
        int[] requestsAffectedRecords = invoicesPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO invoices completed, affected records size = [{}]", requestsAffectedRecords.length);
        return invoicesPreparedStatement;
    }

    private PreparedStatement batchInvoiceStatuses(List<StatusData> updateStatuses) throws SQLException {
        logger.info("-----------------START update invoices table from JSON object:[updateStatus]-----------------");
        PreparedStatement invoicesUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE invoices SET boxQty = ?, invoiceStatusID = ?, commentForStatus = ?, lastStatusUpdated = ? WHERE invoiceIDExternal = ? AND dataSourceID = ?;"
        );

        for (StatusData updateStatus : updateStatuses) {
            String invoiceIdExternal = updateStatus.getRequestId(); // TODO should be invoiceID
            Long numBoxes = updateStatus.getNumBoxes();
            String invoiceStatus = updateStatus.getStatus();
            Date timeOutStatus = updateStatus.getTimeOutStatus();
            String comment = updateStatus.getComment();
            if (numBoxes == null)
                invoicesUpdatePreparedStatement.setNull(1, Types.INTEGER);
            else {
                invoicesUpdatePreparedStatement.setLong(1, numBoxes);
            }
            if (invoiceStatus == null) {
                logger.warn("invoiceStatus for invoiceId = [{}] is null, new invoiceStatus = [CREATED]", invoiceIdExternal);
                invoiceStatus = "CREATED";
            }
            invoicesUpdatePreparedStatement.setString(2, invoiceStatus);
            invoicesUpdatePreparedStatement.setString(3, comment);
            invoicesUpdatePreparedStatement.setDate(4, timeOutStatus);
            invoicesUpdatePreparedStatement.setString(5, invoiceIdExternal);
            invoicesUpdatePreparedStatement.setString(6, LOGIST_1C);

            invoicesUpdatePreparedStatement.addBatch();
        }
        int[] requestsAffectedRecords = invoicesUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE invoices completed, affected records size = [{}]", requestsAffectedRecords.length);
        return invoicesUpdatePreparedStatement;
    }

    private PreparedStatement[] batchRouteLists(List<RouteListsData> updateRouteLists) throws SQLException {
        logger.info("-----------------START update routeLists and invoices table from JSON object:[updateRouteLists]-----------------");

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
                        "  routeID           = VALUES(routeID);", Statement.RETURN_GENERATED_KEYS
        );

        // update invoices data
        PreparedStatement invoicesUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE invoices SET " +
                        "routeListID = (SELECT route_lists.routeListID FROM route_lists WHERE route_lists.routeListIDExternal = ? AND route_lists.dataSourceID = ?)," +
                        "warehousePointID = (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?)" +
                        "  WHERE invoices.invoiceIDExternal = ? AND invoices.dataSourceID = ?;"

        );

        for (RouteListsData updateRouteList : updateRouteLists) {
            String routeListId = updateRouteList.getRouteListId();
            String directId = updateRouteList.getDirectId();
            boolean isDirectIdExists = !directId.equals("NULL");

            Set<String> invoices = updateRouteList.getInvoices(); // list of invoices inside routeList
            String pointDepartureId = updateRouteList.getPointDepartureId(); // pointIDExternal

            if (isDirectIdExists) {
                String routeListNumber = updateRouteList.getRouteListNumber();
                java.sql.Date creationDate = updateRouteList.getRouteListDate();
                java.sql.Date departureDate = updateRouteList.getDepartureDate();
                String forwarderId = updateRouteList.getForwarderId();
                String driverId = updateRouteList.getDriverId();
                String pointArrivalId = updateRouteList.getPointArrivalId(); // TODO not used pointIDExternal
                String status = updateRouteList.getStatus();

                routeListsInsertPreparedStatement.setString(1, routeListId);
                routeListsInsertPreparedStatement.setString(2, LOGIST_1C);
                routeListsInsertPreparedStatement.setString(3, routeListNumber);
                routeListsInsertPreparedStatement.setDate(4, creationDate);
                routeListsInsertPreparedStatement.setDate(5, departureDate);
                routeListsInsertPreparedStatement.setNull(6, Types.INTEGER); // palletsQty
                routeListsInsertPreparedStatement.setString(7, forwarderId); // palletsQty
                routeListsInsertPreparedStatement.setString(8, driverId);
                routeListsInsertPreparedStatement.setString(9, null); // driverPhoneNumber
                routeListsInsertPreparedStatement.setString(10, null); // license plate
                routeListsInsertPreparedStatement.setString(11, status); // license plate
                routeListsInsertPreparedStatement.setString(12, directId);
                routeListsInsertPreparedStatement.setString(13, LOGIST_1C);
                routeListsInsertPreparedStatement.addBatch();
            }

            for(Object invoiceIDExternalAsObject: invoices) {
                String invoiceIDExternal = (String) invoiceIDExternalAsObject;
                invoicesUpdatePreparedStatement.setString(1, routeListId);
                invoicesUpdatePreparedStatement.setString(2, LOGIST_1C);
                invoicesUpdatePreparedStatement.setString(3, pointDepartureId);
                invoicesUpdatePreparedStatement.setString(4, LOGIST_1C);
                invoicesUpdatePreparedStatement.setString(5, invoiceIDExternal);
                invoicesUpdatePreparedStatement.setString(6, LOGIST_1C);
                invoicesUpdatePreparedStatement.addBatch();
            }

        }

        int[] routeListsAffectedRecords = routeListsInsertPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO route_lists completed, affected records size = [{}]", routeListsAffectedRecords.length);

        int[] invoicesAffectedRecords = invoicesUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE routeListID and warehousePoint for invoices completed, affected records size = [{}]", invoicesAffectedRecords.length);

        return new PreparedStatement[]{routeListsInsertPreparedStatement, invoicesUpdatePreparedStatement};
    }

}
