package ru.logist.sbat.db;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

public class InsertTransactionScript {
    private static final Logger logger = LogManager.getLogger();

    private final Connection connection;
    private final JSONObject jsonObject;

    public InsertTransactionScript(Connection connection, JSONObject jsonObject) {
        this.connection = connection;
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


        PreparedStatement
                updatePointsPrepStatement = null,
                updateRoutesPrepStatement = null,
                updateMarketAgentUsersPrepStatement = null,
                updateClientsPrepStatement = null;
        try {
            updatePointsPrepStatement = batchPointsData(updatePointsArray);
            updateRoutesPrepStatement = batchRoutesData(updateRoutesArray);
            updateMarketAgentUsersPrepStatement = batchMarketAgentUser(updateMarketAgentUsersArray);
            updateClientsPrepStatement = batchClients(updateClientsArray);
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
        }

    }

    private PreparedStatement batchPointsData(JSONArray updatePointsArray) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO points (pointIDExternal, pointName, address, email, pointTypeID, responsiblePersonId) VALUES (?,?,?,?,?,?)"
        );

        for (Object updatePoint : updatePointsArray) {

            String pointIdExternalAsString = (String)((JSONObject) updatePoint).get("pointId");
            String responsiblePersonIdAsString = (String)((JSONObject) updatePoint).get("responsiblePersonId");
            Integer pointIDExternal = Integer.parseInt(pointIdExternalAsString);
            Integer responsiblePersonId;
            try {
                responsiblePersonId = Integer.parseInt(responsiblePersonIdAsString);
            } catch (NumberFormatException e) {
                responsiblePersonId = null;
            }
            String pointName = (String)((JSONObject) updatePoint).get("pointName");
            String pointAddress = (String)((JSONObject) updatePoint).get("pointAdress");
            String pointType = (String)((JSONObject) updatePoint).get("pointType");
            String pointEmail = (String)((JSONObject) updatePoint).get("pointEmail");

            preparedStatement.setInt(1, pointIDExternal);
            preparedStatement.setString(2, pointName);
            preparedStatement.setString(3, pointAddress);
            preparedStatement.setString(4, pointEmail);
            preparedStatement.setString(5, pointType);
            if (responsiblePersonId != null)
                preparedStatement.setInt(6, responsiblePersonId);
            else
                preparedStatement.setInt(6, 0);
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
                "INSERT INTO routes (directionIDExternal, routeName) VALUES (?,?)"
        );

        for (Object updateRoute : updateRoutesArray) {
            String directionIDExternal = (String)((JSONObject) updateRoute).get("directId");
            String directionName = (String)((JSONObject) updateRoute).get("directName");

            String uniqueDirectionName = getUniqueDirectionName(allRouteNames, directionName);
            allRouteNames.add(uniqueDirectionName);

            preparedStatement.setString(1, directionIDExternal);
            preparedStatement.setString(2, uniqueDirectionName);
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
                "INSERT INTO users (userIDExternal, userName, email, phoneNumber, salt, passAndSalt, userRoleID) VALUES (?,?,?,?,?,?,?);"
        );

        for (Object updateUser : updateMarketAgents) {
            String userIDExternal = (String)((JSONObject) updateUser).get("traderId");
            String userName = (String)((JSONObject) updateUser).get("traderName");
            String email = (String)((JSONObject) updateUser).get("traderEMail");
            String phoneNumber = (String)((JSONObject) updateUser).get("traderPhone");
            String salt = RandomStringUtils.randomAlphanumeric(16);
            String pass = RandomStringUtils.randomAlphanumeric(8);
            logger.info("generated password for new user [{}] = [{}]", userName, pass);
            String passAndSaltMD5 = md5(md5(pass) + salt);
            preparedStatement.setString(1, userIDExternal);
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
                "INSERT INTO clients (clientIDExternal, clientName, INN) VALUES (?,?,?);"
        );

        for (Object updateClient : updateClients) {
            String clientIDExternal = (String)((JSONObject) updateClient).get("clientId");
            String clientName = (String)((JSONObject) updateClient).get("clientName");
            String inn = (String)((JSONObject) updateClient).get("clientINN");
            preparedStatement.setString(1, clientIDExternal);
            preparedStatement.setString(2, clientName);
            preparedStatement.setString(3, inn);
            preparedStatement.addBatch();
        }
        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT INTO clients completed, affected records = [{}]", Arrays.toString(affectedRecords));
        return preparedStatement;
    }


}
