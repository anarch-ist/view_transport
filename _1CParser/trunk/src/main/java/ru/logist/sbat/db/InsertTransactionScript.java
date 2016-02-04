package ru.logist.sbat.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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


        PreparedStatement updatePointsPrepStatement = null;
        try {

            updatePointsPrepStatement = batchPointsData(updatePointsArray);


            connection.commit();

        } catch(Exception e) {
            logger.error(e);
            DBUtils.rollbackQuietly(connection);
        } finally {
            DBUtils.closeStatementQuietly(updatePointsPrepStatement);
        }

    }

    public PreparedStatement batchPointsData(JSONArray updatePointsArray) throws SQLException {
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

            int[] affectedRecords = preparedStatement.executeBatch();

        }
        return preparedStatement;
    }
}
