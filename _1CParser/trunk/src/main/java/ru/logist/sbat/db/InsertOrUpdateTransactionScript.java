package ru.logist.sbat.db;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.bidimap.DualTreeBidiMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * В случае, если ID совпали, с уже существующим - то делается UPDATE, если же такого ID нет, то делается INSERT
 */
public class InsertOrUpdateTransactionScript {

    private static final Logger logger = LogManager.getLogger();
    public static final String LOGIST_1C = "LOGIST_1C";
    private final Connection connection;
    private final JSONObject jsonObject;
    private InsertOrUpdateResult insertOrUpdateResult;

    public InsertOrUpdateTransactionScript(Connection connection, JSONObject jsonObject) {
        this.connection = connection;
        this.jsonObject = jsonObject;
    }

    /**
     * this method may run only after updateData
     * @return
     */
    public InsertOrUpdateResult getResult() {
        if (insertOrUpdateResult == null)
            throw new IllegalStateException("you must start method updateData() before launch this method");
        return insertOrUpdateResult;
    }

    public void updateData() {
        JSONObject dataFrom1C = (JSONObject) jsonObject.get("dataFrom1C");
        String server = (String)dataFrom1C.get("server");
        logger.info(server);
        Integer packageNumber = Integer.parseInt(String.valueOf(dataFrom1C.get("packageNumber")));
        logger.info(packageNumber);
        logger.info(dataFrom1C.get("created"));
        insertOrUpdateResult = new InsertOrUpdateResult();
        insertOrUpdateResult.setServer(server);
        insertOrUpdateResult.setPackageNumber(packageNumber);


        JSONObject packageData = (JSONObject) dataFrom1C.get("packageData");
        JSONArray updatePoints = (JSONArray) packageData.get("updatePoints");
        JSONArray updateDirections = (JSONArray) packageData.get("updateDirections");
        JSONArray updateTrader = (JSONArray) packageData.get("updateTrader");
        JSONArray updateClients = (JSONArray) packageData.get("updateClients");
        JSONArray updateAddress = (JSONArray) packageData.get("updateAddress");
        JSONArray updateRequests = (JSONArray) packageData.get("updateRequests");
        JSONArray updateStatuses = (JSONArray) packageData.get("updateStatus");
        JSONArray updateRouteLists = (JSONArray) packageData.get("updateRouteLists");

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
            updateExchangePrepStatement = batchPackageData(dataFrom1C);
            updatePointsPrepStatement = batchPointsData(updatePoints, updateAddress);
            updateRoutesPrepStatement = batchRoutesData(updateDirections);
            updateMarketAgentUsersPrepStatement = batchMarketAgentUser(updateTrader);
            updateClientsPrepStatement = batchClients(updateClients);
            updateRequestsPrepStatement = batchRequests(updateRequests);
            updateInvoicesPrepStatement = batchInvoices(updateRequests);
            updateInvoicesStatusesPrepStatement = batchInvoiceStatuses(updateStatuses);
            routeListsInsertPreparedStatements = batchRouteLists(updateRouteLists);
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

    private static final DateTimeFormatter dateTimeSQLFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss 'GMT+'0");
    private PreparedStatement batchPackageData(JSONObject dataFrom1C) throws SQLException {
        logger.info("-----------------START update exchange table from JSON object:[dataFrom1C]-----------------");
        PreparedStatement preparedStatement =  connection.prepareStatement(
                "INSERT INTO exchange (packageNumber, serverName, dataSource, packageCreated, packageData)\n" +
                        "VALUE (?, ?, ?, ?, ?);"
        );
        int packageNumber = Integer.parseInt(String.valueOf(dataFrom1C.get("packageNumber")));
        preparedStatement.setInt(1, packageNumber);
        preparedStatement.setString(2, (String)dataFrom1C.get("server"));
        preparedStatement.setString(3, LOGIST_1C);
        preparedStatement.setDate(4, Date.valueOf(LocalDate.parse((String)dataFrom1C.get("created"), dateTimeSQLFormatter)));
        preparedStatement.setString(5, dataFrom1C.toString());
        preparedStatement.executeUpdate();
        logger.info("INSERT INTO exchange table completed packageNumber = [{}]", packageNumber);
        return preparedStatement;
    }

    private PreparedStatement batchPointsData(JSONArray updatePointsArray, JSONArray updateAddresses) throws SQLException {
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
        for (Object updatePoint : updatePointsArray) {

            String pointIdExternal = (String)((JSONObject) updatePoint).get("pointId");
            String responsiblePersonId = (String)((JSONObject) updatePoint).get("responsiblePersonId");
            String pointName = (String)((JSONObject) updatePoint).get("pointName");
            String pointAddress = (String)((JSONObject) updatePoint).get("pointAdress");
            String pointType = (String)((JSONObject) updatePoint).get("pointType");
            String pointEmail = (String)((JSONObject) updatePoint).get("pointEmail");

            pointsStatement.setString(1, pointIdExternal);
            pointsStatement.setString(2, LOGIST_1C);
            pointsStatement.setString(3, pointName);
            pointsStatement.setString(4, pointAddress);
            pointsStatement.setString(5, pointEmail);
            pointsStatement.setString(6, pointType);
            pointsStatement.setString(7, responsiblePersonId);
            pointsStatement.addBatch();
        }

        // create or update points from updateAddresses JSON array
        // add only unique points
        Utils.UniqueCheck uniqueCheckAddressId = Utils.getUniqueCheckObject("addressId");
        for (Object updateAddress : updateAddresses) {
            String pointIdExternal = (String)((JSONObject) updateAddress).get("addressId");
            if (!uniqueCheckAddressId.isUnique(pointIdExternal))
                continue;

            String pointName = (String)((JSONObject) updateAddress).get("addressShot");
            String pointAddress = (String)((JSONObject) updateAddress).get("addressFull");

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

    private PreparedStatement batchRoutesData(JSONArray updateRoutesArray) throws SQLException {
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

        for (Object updateRoute : updateRoutesArray) {
            String directionIDExternal = (String)((JSONObject) updateRoute).get("directId");
            String directionName = (String)((JSONObject) updateRoute).get("directName");
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
    private PreparedStatement[] batchMarketAgentUser(JSONArray updateMarketAgents) throws SQLException {
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
        for (Object updateUser : updateMarketAgents) {
            String login = (String)((JSONObject) updateUser).get("traderId");
            if (!uniqueCheckLogins.isUnique(login))
                continue;
            String userName = (String)((JSONObject) updateUser).get("traderName");
            String email = (String)((JSONObject) updateUser).get("traderEMail");
            String phoneNumber = (String)((JSONObject) updateUser).get("traderPhone");

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

    private PreparedStatement batchClients(JSONArray updateClients) throws SQLException {
        logger.info("-----------------START update clients table from JSON object:[clients]-----------------");
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN)\n" +
                        "  VALUE (?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  clientName = VALUES(clientName),\n" +
                        "  INN        = VALUES(INN);"
        );
        Utils.UniqueCheck uniqueCheckClientId = Utils.getUniqueCheckObject("clientId");
        for (Object updateClient : updateClients) {
            String clientIDExternal = (String)((JSONObject) updateClient).get("clientId");
            if (!uniqueCheckClientId.isUnique(clientIDExternal))
                continue;
            String clientName = (String)((JSONObject) updateClient).get("clientName");
            String inn = (String)((JSONObject) updateClient).get("clientINN");
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

    private PreparedStatement batchRequests(JSONArray updateRequests) throws SQLException {
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
        for (Object updateRequest : updateRequests) {
            String requestIDExternal = (String)((JSONObject) updateRequest).get("requestId");
            String requestNumber = (String)((JSONObject) updateRequest).get("requestNumber");
            String requestDateAsString = (String)((JSONObject) updateRequest).get("requestDate");
            String login = (String)((JSONObject) updateRequest).get("traderId"); //userID
            String destinationPointIdExternal = (String)((JSONObject) updateRequest).get("addressId"); //destinationPoint


            String clientIdExternal = (String)((JSONObject) updateRequest).get("clientId");
            requestsPreparedStatement.setString(1, requestIDExternal);
            requestsPreparedStatement.setString(2, LOGIST_1C);
            requestsPreparedStatement.setString(3, requestNumber);
            requestsPreparedStatement.setDate(4, Utils.getSqlDateFromString(requestDateAsString));
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

    private PreparedStatement batchInvoices(JSONArray updateRequests) throws SQLException {
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

        for (Object updateRequest : updateRequests) {
            String requestIDExternal = (String)((JSONObject) updateRequest).get("requestId");
            //String invoiceIdExternal = (String)((JSONObject) updateRequest).get("invoiceNumber");
            //TODO temporally use requestId instead invoiceNumber
            String invoiceIdExternal = (String)((JSONObject) updateRequest).get("requestId");
            String documentNumber = (String)((JSONObject) updateRequest).get("documentNumber");
            if (documentNumber == null) documentNumber = "";

            String firma = (String)((JSONObject) updateRequest).get("firma");
            String storage = (String)((JSONObject) updateRequest).get("storage");
            String contactName = (String)((JSONObject) updateRequest).get("contactName");
            String contactPhone = (String)((JSONObject) updateRequest).get("contactPhone");
            String deliveryOption = (String)((JSONObject) updateRequest).get("deliveryOption");

            String invoiceDateAsString = (String)((JSONObject) updateRequest).get("invoiceDate");
            java.sql.Date creationDate = null;
            try {
                creationDate = Utils.getSqlDateFromString(invoiceDateAsString);
            } catch (DateTimeParseException e) {
                logger.warn("invoiceDate for requestId = [{}] has incompatible format with value = [{}]", requestIDExternal, invoiceDateAsString);
            }
            String documentDateAsString = (String)((JSONObject) updateRequest).get("documentDate");
            java.sql.Date documentDate =  null;
            try {
                documentDate = Utils.getSqlDateFromString(documentDateAsString);
            } catch (DateTimeParseException e) {
                logger.warn("documentDate for requestId = [{}] has incompatible format with value = [{}]", requestIDExternal, documentDateAsString);
            }
            String deliveryDateAsString = (String)((JSONObject) updateRequest).get("deliveryDate");
            java.sql.Date deliveryDate =  null;
            try {
                deliveryDate = Utils.getSqlDateFromString(deliveryDateAsString);
            } catch (DateTimeParseException e) {
                logger.warn("deliveryDate for requestId = [{}] has incompatible format with value = [{}]", requestIDExternal, deliveryDateAsString);
            }

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

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uu.MM.dd,HH:mm:ss");
    private PreparedStatement batchInvoiceStatuses(JSONArray updateStatuses) throws SQLException {
        logger.info("-----------------START update invoices table from JSON object:[updateStatus]-----------------");
        PreparedStatement invoicesUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE invoices SET boxQty = ?, invoiceStatusID = ?, commentForStatus = ?, lastStatusUpdated = ? WHERE invoiceIDExternal = ? AND dataSourceID = ?;"
        );

        for (Object updateStatus : updateStatuses) {
            String invoiceIdExternal = (String)((JSONObject) updateStatus).get("requestId"); // should be invoiceID
            Long numBoxes = (Long)((JSONObject) updateStatus).get("num_boxes");
            String invoiceStatus = (String)((JSONObject) updateStatus).get("status");


            java.sql.Date timeOutStatus = null;
            String timeOutStatusAsString = (String)((JSONObject) updateStatus).get("timeOutStatus");
            if (timeOutStatusAsString != null) {
                try {
                    LocalDate requestDate = LocalDate.parse(timeOutStatusAsString, dateTimeFormatter);
                    Date date = Date.valueOf(requestDate);
                    timeOutStatus = new Date(date.getTime());
                } catch (DateTimeParseException e) {
                    logger.warn("timeOutStatus for invoiceId = [{}] has empty value", invoiceIdExternal);
                }
            }

            String comment = (String)((JSONObject) updateStatus).get("Comment");

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

    private PreparedStatement[] batchRouteLists(JSONArray updateRouteLists) throws SQLException {
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

        for (Object updateRouteList : updateRouteLists) {
            String routeListId = (String)((JSONObject) updateRouteList).get("routerSheetId");
            String directId = (String)((JSONObject) updateRouteList).get("directId");
            boolean isDirectIdExists = !directId.equals("NULL");
            // импортируем только начальный пункт накладной
//            if (directId.equals("NULL")) {
//                logger.warn("directId is NULL, routeList = [{}] not inserted", routeListId);
//                continue;
//            } else {
//
//            }
            JSONArray invoices = (JSONArray) ((JSONObject) updateRouteList).get("invoices"); // list of invoices inside routeList
            String pointDepartureId = (String)((JSONObject) updateRouteList).get("pointDepartureId"); // pointIDExternal

            if (isDirectIdExists) {
                String routeListNumber = (String)((JSONObject) updateRouteList).get("routerSheetNumber");
                java.sql.Date creationDate = null;
                String routeListDateAsString = (String)((JSONObject) updateRouteList).get("routerSheetDate");
                try {
                    creationDate = Utils.getSqlDateFromString(routeListDateAsString);
                } catch (DateTimeParseException e) {
                    logger.warn("routeListDate for routeListId = [{}] has incompatible format with value = [{}]", routeListId, routeListDateAsString);
                }
                java.sql.Date departureDate = null;
                String departureDateAsString = (String)((JSONObject) updateRouteList).get("departureDate");
                try {
                    departureDate = Utils.getSqlDateFromString(routeListDateAsString);
                } catch (DateTimeParseException e) {
                    logger.warn("departureDate for routeListId = [{}] has incompatible format with value = [{}]", routeListId, routeListDateAsString);
                }
                String forwarderId = (String)((JSONObject) updateRouteList).get("forwarderId");
                String driverId = (String)((JSONObject) updateRouteList).get("driverId");
                String pointArrivalId = (String)((JSONObject) updateRouteList).get("pointArrivalId"); // pointIDExternal
                String status = (String)((JSONObject) updateRouteList).get("status");


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
