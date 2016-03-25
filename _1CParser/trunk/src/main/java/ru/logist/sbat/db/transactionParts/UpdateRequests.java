package ru.logist.sbat.db.transactionParts;


import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.beans.RequestsData;

import java.sql.*;
import java.util.*;

public class UpdateRequests extends TransactionPart{
    private List<RequestsData> updateRequests;

    public UpdateRequests(List<RequestsData> updateRequests) {
        this.updateRequests = updateRequests;
    }

    @Override
    PreparedStatement executePart() throws SQLException {
        logger.info("-----------------START update requests table from JSON object:[updateRequestsTable]-----------------");
        Map<String, String> allPoints = selectAllPoints();
        Set<String> allClientsIdExternal = selectAllClientsIdExternal();
        Set<String> allUsersIDExternal = selectAllUsersIdExternal(InsertOrUpdateTransactionScript.LOGIST_1C);

        PreparedStatement requestsPreparedStatement = connection.prepareStatement(
                "INSERT INTO requests\n" +
                        "  VALUE\n" +
                        "  (NULL,\n" +
                        "    ?, ?, ?, ?,\n" +
                        "    (SELECT clients.clientID FROM clients WHERE clients.clientIDExternal = ? AND clients.dataSourceID = ?),\n" +
                        "    (SELECT points.pointID FROM points WHERE points.pointIDExternal = ? AND points.dataSourceID = ?),\n" +
                        "    (SELECT users.userID FROM users WHERE users.userIDExternal = ? AND users.dataSourceID = ?),\n" +
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
            requestsPreparedStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            requestsPreparedStatement.setString(3, updateRequest.getRequestNumber());
            requestsPreparedStatement.setDate(4,  updateRequest.getRequestDate());

            String clientId = updateRequest.getClientId();
            if (!allClientsIdExternal.contains(clientId)) {
                throw new DBCohesionException(Util.getParameterizedString("requestId {} has clientId = {} that is not contained in clients table.", updateRequest.getRequestId(), clientId));
            } else
                requestsPreparedStatement.setString(5, clientId);
            requestsPreparedStatement.setString(6, InsertOrUpdateTransactionScript.LOGIST_1C);

            String addressId = updateRequest.getAddressId();
            if (!allPoints.containsKey(addressId)) {
                logger.warn("requestId [{}] has addressId = [{}] that is not contained in points table. Set NULL for pointID", updateRequest.getRequestId(), addressId);
                requestsPreparedStatement.setString(7, null);
            }
            else
                requestsPreparedStatement.setString(7, addressId);
            requestsPreparedStatement.setString(8, InsertOrUpdateTransactionScript.LOGIST_1C);

            String traderId = updateRequest.getTraderId();
            if (!allUsersIDExternal.contains(traderId)) {
                throw new DBCohesionException(Util.getParameterizedString("requestId {} has traderId = {} that is not contained in users table.", updateRequest.getRequestId(), traderId));
            }
            else
                requestsPreparedStatement.setString(9, traderId);
            requestsPreparedStatement.setString(10, InsertOrUpdateTransactionScript.LOGIST_1C);

            requestsPreparedStatement.setString(11, updateRequest.getInvoiceNumber());
            requestsPreparedStatement.setDate(12, updateRequest.getInvoiceDate());
            requestsPreparedStatement.setString(13, updateRequest.getDocumentNumber());
            requestsPreparedStatement.setDate(14, updateRequest.getDocumentDate());
            requestsPreparedStatement.setString(15, updateRequest.getFirma());
            requestsPreparedStatement.setString(16, updateRequest.getStorage());
            requestsPreparedStatement.setString(17, updateRequest.getContactName());
            requestsPreparedStatement.setString(18, updateRequest.getContactPhone());
            requestsPreparedStatement.setString(19, updateRequest.getDeliveryOption());
            requestsPreparedStatement.setDate(20, updateRequest.getDeliveryDate());
            requestsPreparedStatement.setNull(21, Types.INTEGER); //boxQty
            requestsPreparedStatement.setNull(22, Types.INTEGER); //weight
            requestsPreparedStatement.setNull(23, Types.INTEGER); //volume
            requestsPreparedStatement.setBigDecimal(24, null); //goodsCost
            requestsPreparedStatement.setString(25, "заявка создана"); //commentForStatus
            requestsPreparedStatement.addBatch();
        }
        int[] requestsAffectedRecords = requestsPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO requests completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestsPreparedStatement;
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

    private Set<String> selectAllClientsIdExternal() throws SQLException {
        Set<String> result = new HashSet<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT clientIDExternal FROM clients;");
        while (resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        return result;
    }

    private Set<String> selectAllUsersIdExternal(String dataSourceId) throws SQLException {
        Set<String> result = new HashSet<>();
        PreparedStatement statement = connection.prepareStatement("SELECT userIDExternal FROM users WHERE dataSourceID = ?");
        statement.setString(1, dataSourceId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            result.add(resultSet.getString(1));
        }
        return result;
    }
}
