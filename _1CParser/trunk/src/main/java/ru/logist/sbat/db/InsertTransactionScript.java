package ru.logist.sbat.db;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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


        PreparedStatement
                updatePointsPrepStatement = null,
                updateRoutesPrepStatement = null,
                updateMarketAgentUsersPrepStatement = null,
                updateClientsPrepStatement = null,
                updateRequestsPrepStatement = null;
        try {
            updatePointsPrepStatement = batchPointsData(updatePointsArray, updateAddresses);
            updateRoutesPrepStatement = batchRoutesData(updateRoutesArray);
            updateMarketAgentUsersPrepStatement = batchMarketAgentUser(updateMarketAgentUsersArray);
            updateClientsPrepStatement = batchClients(updateClientsArray);
            updateRequestsPrepStatement = batchRequestsAndInvoices(updateRequests);
            connection.commit();

        } catch(Exception e) {
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
        LocalDate requestDate = LocalDate.parse(dateString, DateTimeFormatter.BASIC_ISO_DATE);
        Date date = Date.valueOf(requestDate);
        return new java.sql.Date(date.getTime());
    }
    //
    private PreparedStatement batchRequestsAndInvoices(JSONArray updateRequests) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
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
            // externals, foreign keys
            String login = (String)((JSONObject) updateRequest).get("traderId"); //userID
            String pointIdExternal = (String)((JSONObject) updateRequest).get("addressId"); //pointID
            String clientIdExternal = (String)((JSONObject) updateRequest).get("clientId");

            preparedStatement.setString(1, requestIDExternal);
            preparedStatement.setString(2, LOGIST_1C);
            preparedStatement.setString(3, requestNumber);

            preparedStatement.setDate(  4, getSqlDateFromString(requestDateAsString));
            preparedStatement.setString(5, login);
            preparedStatement.setString(6, clientIdExternal);
            preparedStatement.setString(7, LOGIST_1C);
            preparedStatement.setString(8, pointIdExternal);
            preparedStatement.setString(9, LOGIST_1C);
            preparedStatement.addBatch();
        }



        PreparedStatement preparedStatement2 = connection.prepareStatement(

        );

        for (Object updateRequest : updateRequests) {
            String invoiceNumber = (String)((JSONObject) updateRequest).get("invoiceNumber");
            String invoiceDateAsString = (String)((JSONObject) updateRequest).get("invoiceDate");
            java.sql.Date invoiceDate = getSqlDateFromString(invoiceDateAsString);
            String documentNumber = (String)((JSONObject) updateRequest).get("documentNumber");
            String documentDateAsString = (String)((JSONObject) updateRequest).get("documentDate");
            java.sql.Date documentDate = getSqlDateFromString(documentDateAsString);
            String firma = (String)((JSONObject) updateRequest).get("firma");
            String storage = (String)((JSONObject) updateRequest).get("storage");
            String contactName = (String)((JSONObject) updateRequest).get("contactName");
            String contactPhone = (String)((JSONObject) updateRequest).get("contactPhone");
            String deliveryOption = (String)((JSONObject) updateRequest).get("deliveryOption");
            String deliveryDateAsString = (String)((JSONObject) updateRequest).get("deliveryDate");
            java.sql.Date deliveryDate = getSqlDateFromString(deliveryDateAsString);








            String requestDateAsString = (String)((JSONObject) updateRequest).get("requestDate");
            // externals, foreign keys
            String login = (String)((JSONObject) updateRequest).get("traderId"); //userID
            String pointIdExternal = (String)((JSONObject) updateRequest).get("addressId"); //pointID
            String clientIdExternal = (String)((JSONObject) updateRequest).get("clientId");

            preparedStatement.setString(1, requestIDExternal);
            preparedStatement.setString(2, LOGIST_1C);
            preparedStatement.setString(3, requestNumber);

            preparedStatement.setDate(  4, getSqlDateFromString(requestDateAsString));
            preparedStatement.setString(5, login);
            preparedStatement.setString(6, clientIdExternal);
            preparedStatement.setString(7, LOGIST_1C);
            preparedStatement.setString(8, pointIdExternal);
            preparedStatement.setString(9, LOGIST_1C);
            preparedStatement.addBatch();
        }


        int[] affectedRecords = preparedStatement.executeBatch();




        logger.info("INSERT INTO requests and invoices completed, affected records = [{}]", Arrays.toString(affectedRecords));
        return preparedStatement;
    }


}
