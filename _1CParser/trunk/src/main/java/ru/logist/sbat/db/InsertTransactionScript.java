package ru.logist.sbat.db;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InsertTransactionScript {

    private static final Logger logger = LogManager.getLogger();
    public static final String LOGIST_1C = "LOGIST_1C";
    private final Connection connection;
    private final JSONObject jsonObject;

    public InsertTransactionScript(Connection connection, JSONObject jsonObject) {
        this.connection = connection;
//        try {
//            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        this.jsonObject = jsonObject;
    }

    public void updateData() {

        JSONObject dataFrom1C = (JSONObject) jsonObject.get("dataFrom1C");
        System.out.println(dataFrom1C.get("server"));
        System.out.println(dataFrom1C.get("packageNumber"));
        System.out.println(dataFrom1C.get("created"));

        JSONObject packageData = (JSONObject) dataFrom1C.get("packageData");
        JSONArray updatePointsArray = (JSONArray) packageData.get("updatePoints");
        JSONArray updateRoutesArray = (JSONArray) packageData.get("updateDirections");
        JSONArray updateMarketAgentUsersArray = (JSONArray) packageData.get("updateTrader");
        JSONArray updateClientsArray = (JSONArray) packageData.get("updateClients");
        JSONArray updateAddresses = (JSONArray) packageData.get("updateAddress");
        JSONArray updateRequests = (JSONArray) packageData.get("updateRequests");
        JSONArray updateStatuses = (JSONArray) packageData.get("updateStatus");
        JSONArray updateRouteLists = (JSONArray) packageData.get("updateRouteLists");

        PreparedStatement
                updatePointsPrepStatement = null,
                updateRoutesPrepStatement = null,
                updateMarketAgentUsersPrepStatement = null,
                updateClientsPrepStatement = null,
                updateRequestsPrepStatement = null,
                updateInvoicesPrepStatement = null,
                updateInvoicesStatusesPrepStatement = null;
        PreparedStatement[] routeListsInsertPreparedStatements = null;

        try {
            updatePointsPrepStatement = batchPointsData(updatePointsArray, updateAddresses);
            updateRoutesPrepStatement = batchRoutesData(updateRoutesArray);
            updateMarketAgentUsersPrepStatement = batchMarketAgentUser(updateMarketAgentUsersArray);
            updateClientsPrepStatement = batchClients(updateClientsArray);
            updateRequestsPrepStatement = batchRequests(updateRequests);
            updateInvoicesPrepStatement = batchInvoices(updateRequests);
            updateInvoicesStatusesPrepStatement = batchInvoiceStatuses(updateStatuses);
            routeListsInsertPreparedStatements = batchRouteLists(updateRouteLists);
            connection.commit();

        } catch(Exception e) {
            e.printStackTrace();
            logger.error(e);
            logger.error("start ROLLBACK");
            DBUtils.rollbackQuietly(connection);
            logger.error("end ROLLBACK");
        } finally {
            DBUtils.closeStatementQuietly(updatePointsPrepStatement);
            DBUtils.closeStatementQuietly(updateRoutesPrepStatement);
            DBUtils.closeStatementQuietly(updateMarketAgentUsersPrepStatement);
            DBUtils.closeStatementQuietly(updateClientsPrepStatement);
            DBUtils.closeStatementQuietly(updateRequestsPrepStatement);
            DBUtils.closeStatementQuietly(updateInvoicesPrepStatement);
            DBUtils.closeStatementQuietly(updateInvoicesStatusesPrepStatement);
            DBUtils.closeStatementQuietly(routeListsInsertPreparedStatements != null ? routeListsInsertPreparedStatements[0] : null);
            DBUtils.closeStatementQuietly(routeListsInsertPreparedStatements != null ? routeListsInsertPreparedStatements[1] : null);
            DBUtils.closeStatementQuietly(routeListsInsertPreparedStatements != null ? routeListsInsertPreparedStatements[2] : null);
        }
    }

    private PreparedStatement batchPointsData(JSONArray updatePointsArray, JSONArray updateAddresses) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO points (pointIDExternal, dataSourceID, pointName, address, email, pointTypeID, responsiblePersonId) VALUES (?,?,?,?,?,?,?)"
        );

        for (Object updatePoint : updatePointsArray) {

            String pointIdExternal = (String)((JSONObject) updatePoint).get("pointId");
            String responsiblePersonId = (String)((JSONObject) updatePoint).get("responsiblePersonId");
            String pointName = (String)((JSONObject) updatePoint).get("pointName");
            String pointAddress = (String)((JSONObject) updatePoint).get("pointAdress");
            String pointType = (String)((JSONObject) updatePoint).get("pointType");
            String pointEmail = (String)((JSONObject) updatePoint).get("pointEmail");

            preparedStatement.setString(1, pointIdExternal);
            preparedStatement.setString(2, LOGIST_1C);
            preparedStatement.setString(3, pointName);
            preparedStatement.setString(4, pointAddress);
            preparedStatement.setString(5, pointEmail);
            preparedStatement.setString(6, pointType);
            preparedStatement.setString(7, responsiblePersonId);
            preparedStatement.addBatch();
        }

        // add only unique points
        Set<String>  uniquePointIdExternals = new HashSet<>();
        for (Object updateAddress : updateAddresses) {
            String pointIdExternal = (String)((JSONObject) updateAddress).get("addressId");
            if (!uniquePointIdExternals.add(pointIdExternal))
                continue;
            String pointName = (String)((JSONObject) updateAddress).get("addressShot");
            String pointAddress = (String)((JSONObject) updateAddress).get("addressFull");

            preparedStatement.setString(1, pointIdExternal);
            preparedStatement.setString(2, LOGIST_1C);
            preparedStatement.setString(3, pointName);
            preparedStatement.setString(4, pointAddress);
            preparedStatement.setString(5, "");
            preparedStatement.setString(6, "AGENCY");
            preparedStatement.setString(7, "");
            preparedStatement.addBatch();
        }

        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT INTO points completed, affected records = [" + Arrays.toString(affectedRecords) + "]");
        return preparedStatement;
    }

    /**
     * if allRouteNames contains directionName then direction name changed
     * @param allRouteNames
     * @param directionName
     */
    private String getUniqueDirectionName(final Set<String> allRouteNames, final String directionName) {
        if (!allRouteNames.contains(directionName))
            return directionName;
        else {
            int count = 0;
            do {
                count++;
                //generatedDirectionName = directionName + count + "";
            } while (allRouteNames.contains(directionName + count + ""));
            String generatedDirectionName = directionName + count + "";
            logger.warn("direction name [{}] was duplicated, generated direction name = [{}]", directionName, generatedDirectionName);
            return generatedDirectionName;
        }
    }

    private PreparedStatement batchRoutesData(JSONArray updateRoutesArray) throws SQLException {

        // get all routeNames and check for uniqness
        Set<String> allRouteNames = new HashSet<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT routeName FROM routes;");
        while (resultSet.next()) {
            allRouteNames.add(resultSet.getString(1));
        }

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO routes (directionIDExternal, dataSourceID,  routeName) VALUES (?,?,?)"
        );

        for (Object updateRoute : updateRoutesArray) {
            String directionIDExternal = (String)((JSONObject) updateRoute).get("directId");
            String directionName = (String)((JSONObject) updateRoute).get("directName");

            String uniqueDirectionName = getUniqueDirectionName(allRouteNames, directionName);
            allRouteNames.add(uniqueDirectionName);

            preparedStatement.setString(1, directionIDExternal);
            preparedStatement.setString(2, LOGIST_1C);
            preparedStatement.setString(3, uniqueDirectionName);
            preparedStatement.addBatch();
        }
        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT INTO routes completed, affected records = [" + Arrays.toString(affectedRecords) + "]");
        return preparedStatement;
    }

    // 32 digits like in php
    public String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    private static final String userRoleId = "MARKET_AGENT";
    private PreparedStatement batchMarketAgentUser(JSONArray updateMarketAgents) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users (login, userName, email, phoneNumber, salt, passAndSalt, userRoleID) VALUES (?,?,?,?,?,?,?);"
        );
        Set<String> logins = new HashSet<>();
        for (Object updateUser : updateMarketAgents) {
            String login = (String)((JSONObject) updateUser).get("traderId");
            if (!logins.add(login)) // if duplicate do nothing
                continue;
            String userName = (String)((JSONObject) updateUser).get("traderName");
            String email = (String)((JSONObject) updateUser).get("traderEMail");
            String phoneNumber = (String)((JSONObject) updateUser).get("traderPhone");
            String salt = RandomStringUtils.randomAlphanumeric(16);
            String pass = RandomStringUtils.randomAlphanumeric(8);
            logger.info("generated password for new user [{}] = [{}]", userName, pass);
            String passAndSaltMD5 = md5(md5(pass) + salt);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, salt);
            preparedStatement.setString(6, passAndSaltMD5);
            preparedStatement.setString(7, userRoleId);
            preparedStatement.addBatch();
        }
        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT INTO users for [{}]s completed, affected records = [{}]", userRoleId, Arrays.toString(affectedRecords));
        return preparedStatement;
    }


    private PreparedStatement batchClients(JSONArray updateClients) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN) VALUES (?,?,?,?);"
        );
        Set<String> uniqueClientIdExternals = new HashSet<>();
        for (Object updateClient : updateClients) {
            String clientIDExternal = (String)((JSONObject) updateClient).get("clientId");
            if (!uniqueClientIdExternals.add(clientIDExternal))
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
        logger.info("INSERT INTO clients completed, affected records = [{}]", Arrays.toString(affectedRecords));
        return preparedStatement;
    }

    protected java.sql.Date getSqlDateFromString(String dateString) {
        if (dateString == null)
            return null;
        LocalDate requestDate = LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE);
        Date date = Date.valueOf(requestDate);
        return new java.sql.Date(date.getTime());
    }


    private PreparedStatement batchRequests(JSONArray updateRequests) throws SQLException {
        PreparedStatement requestsPreparedStatement = connection.prepareStatement(
                "INSERT INTO requests\n" +
                        "SELECT\n" +
                        "  NULL,\n" +
                        "  ?,\n" +
                        "  ?,\n" +
                        "  ?,\n" +
                        "  ?,\n" +
                        "  (SELECT users.userID FROM users WHERE users.login = ?),\n" +
                        "  (SELECT clients.clientID FROM clients WHERE clients.clientIDExternal = ? AND clients.dataSourceID = ?),\n" +
                        "  (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?)"
        );

        for (Object updateRequest : updateRequests) {
            String requestIDExternal = (String)((JSONObject) updateRequest).get("requestId");
            String requestNumber = (String)((JSONObject) updateRequest).get("requestNumber");
            String requestDateAsString = (String)((JSONObject) updateRequest).get("requestDate");
            String login = (String)((JSONObject) updateRequest).get("traderId"); //userID
            String pointIdExternal = (String)((JSONObject) updateRequest).get("addressId"); //pointID
            String clientIdExternal = (String)((JSONObject) updateRequest).get("clientId");

            requestsPreparedStatement.setString(1, requestIDExternal);
            requestsPreparedStatement.setString(2, LOGIST_1C);
            requestsPreparedStatement.setString(3, requestNumber);
            requestsPreparedStatement.setDate(4, getSqlDateFromString(requestDateAsString));
            requestsPreparedStatement.setString(5, login);
            requestsPreparedStatement.setString(6, clientIdExternal);
            requestsPreparedStatement.setString(7, LOGIST_1C);
            requestsPreparedStatement.setString(8, pointIdExternal);
            requestsPreparedStatement.setString(9, LOGIST_1C);
            requestsPreparedStatement.addBatch();
        }
        int[] requestsAffectedRecords = requestsPreparedStatement.executeBatch();
        logger.info("INSERT INTO requests completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestsPreparedStatement;
    }

    private PreparedStatement batchInvoices(JSONArray updateRequests) throws SQLException {

        PreparedStatement invoicesPreparedStatement = connection.prepareStatement(
            "INSERT INTO invoices\n" +
                    "  VALUE\n" +
                    "  (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), getUserIDByLogin('parser'), 'CREATED',\n" +
                    "  ?, (SELECT requests.requestID FROM requests WHERE requests.requestIDExternal = ? AND requests.dataSourceID = ?), 1, NULL, NULL);"
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
                creationDate = getSqlDateFromString(invoiceDateAsString);
            } catch (DateTimeParseException e) {
                logger.warn("invoiceDate for requestId = [{}] has incompatible format with value = [{}]", requestIDExternal, invoiceDateAsString);
            }
            String documentDateAsString = (String)((JSONObject) updateRequest).get("documentDate");
            java.sql.Date documentDate =  null;
            try {
                documentDate = getSqlDateFromString(documentDateAsString);
            } catch (DateTimeParseException e) {
                logger.warn("documentDate for requestId = [{}] has incompatible format with value = [{}]", requestIDExternal, documentDateAsString);
            }
            String deliveryDateAsString = (String)((JSONObject) updateRequest).get("deliveryDate");
            java.sql.Date deliveryDate =  null;
            try {
                deliveryDate = getSqlDateFromString(deliveryDateAsString);
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
        logger.info("INSERT INTO invoices completed, affected records size = [{}]", requestsAffectedRecords.length);
        return invoicesPreparedStatement;
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uu.MM.dd,HH:mm:ss");
    private PreparedStatement batchInvoiceStatuses(JSONArray updateStatuses) throws SQLException {
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
                logger.warn("invoiceStatus for invoiceId = [{}] is null, new invoiceStatus = [CREATED]");
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
        PreparedStatement[] result = new PreparedStatement[3];

        // create routeLists
        PreparedStatement routeListsInsertPreparedStatement = connection.prepareStatement(
                "INSERT INTO route_lists VALUE (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, getRouteIDByDirectionIDExternal(?,?));", Statement.RETURN_GENERATED_KEYS
        );

        // add data into routePoints
//        PreparedStatement routePointsInsertPreparedStatement = connection.prepareStatement(
//                "INSERT INTO route_points VALUE (NULL, 0, 0, (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?), getRouteIDByDirectionIDExternal(?,?));"
//        );

        // add data into invoices
        PreparedStatement invoicesUpdatePreparedStatement = connection.prepareStatement(
                "UPDATE invoices SET " +
                        "routeListID = (SELECT route_lists.routeListID FROM route_lists WHERE route_lists.routeListIDExternal = ? AND route_lists.dataSourceID = ?)," +
                        "warehousePointID = (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?)" +
                        "  WHERE invoices.invoiceIDExternal = ? AND invoices.dataSourceID = ?;"

        );

        result[0] = routeListsInsertPreparedStatement;
        //result[1] = routePointsInsertPreparedStatement;
        result[2] = invoicesUpdatePreparedStatement;

        for (Object updateRouteList : updateRouteLists) {
            String routeListId = (String)((JSONObject) updateRouteList).get("routerSheetId");
            String directId = (String)((JSONObject) updateRouteList).get("directId");
            if (directId.equals("NULL")) {
                logger.warn("directId is NULL, routeList = [{}] not inserted", routeListId);
                continue;
            }

            String routeListNumber = (String)((JSONObject) updateRouteList).get("routerSheetNumber");
            java.sql.Date creationDate = null;
            String routeListDateAsString = (String)((JSONObject) updateRouteList).get("routerSheetDate");
            try {
                creationDate = getSqlDateFromString(routeListDateAsString);
            } catch (DateTimeParseException e) {
                logger.warn("routeListDate for routeListId = [{}] has incompatible format with value = [{}]", routeListId, routeListDateAsString);
            }
            java.sql.Date departureDate = null;
            String departureDateAsString = (String)((JSONObject) updateRouteList).get("departureDate");
            try {
                departureDate = getSqlDateFromString(routeListDateAsString);
            } catch (DateTimeParseException e) {
                logger.warn("departureDate for routeListId = [{}] has incompatible format with value = [{}]", routeListId, routeListDateAsString);
            }
            String forwarderId = (String)((JSONObject) updateRouteList).get("forwarderId");
            String driverId = (String)((JSONObject) updateRouteList).get("driverId");
            String pointDepartureId = (String)((JSONObject) updateRouteList).get("pointDepartureId"); // pointIDExternal
            String pointArrivalId = (String)((JSONObject) updateRouteList).get("pointArrivalId"); // pointIDExternal

            String status = (String)((JSONObject) updateRouteList).get("status");
            JSONArray invoices = (JSONArray) ((JSONObject) updateRouteList).get("invoices"); // list of invoices inside routeList

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

//            routePointsInsertPreparedStatement.setString(1, pointDepartureId);
//            routePointsInsertPreparedStatement.setString(2, LOGIST_1C);
//            routePointsInsertPreparedStatement.setString(3, directId);
//            routePointsInsertPreparedStatement.setString(4, LOGIST_1C);
//            routePointsInsertPreparedStatement.addBatch();

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
        logger.info("INSERT INTO route_lists completed, affected records size = [{}]", routeListsAffectedRecords.length);

//        int[] routePointsAffectedRecords = routePointsInsertPreparedStatement.executeBatch();
//        logger.info("INSERT INTO routePoints completed, affected records size = [{}]", routePointsAffectedRecords.length);

        int[] invoicesAffectedRecords = invoicesUpdatePreparedStatement.executeBatch();
        logger.info("UPDATE for invoices completed, affected records size = [{}]", invoicesAffectedRecords.length);

        return result;
    }

}
