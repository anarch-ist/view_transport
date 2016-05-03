package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.jsonToBean.beans.RequestsData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class UpdateRequests extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private List<RequestsData> updateRequests;

    public UpdateRequests(List<RequestsData> updateRequests) {
        this.updateRequests = updateRequests;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateRequests.isEmpty())
            return null;
        logger.info("START update requests table from JSON object:[updateRequestsTable]");

        // get All foreign keys that is not null
        BidiMap<String, Integer> allClientsKeyMap = Selects.getInstance().allClientsAsKeyPairs();
        BidiMap<String, Integer> allPointsKeyMap = Selects.getInstance().allPointsAsKeyPairs();
        BidiMap<String, Integer> allUsersKeyMap = Selects.getInstance().allUsersAsKeyPairs();
        Integer parserUserId = Selects.getInstance().selectParserId();

        PreparedStatement requestsPreparedStatement = connection.prepareStatement(
                "INSERT INTO requests(requestIDExternal, dataSourceID, requestNumber, requestDate, clientID," +
                        " destinationPointID, marketAgentUserID, invoiceNumber, invoiceDate," +
                        " documentNumber, documentDate, firma, storage, contactName, contactPhone, deliveryOption, deliveryDate," +
                        " lastStatusUpdated, lastModifiedBy, requestStatusID, commentForStatus)\n" +
                        "  VALUE\n" +
                        "("+
                        " ?," +                        //requestIdExternal   1
                        " ?," +                        //dataSourceId        2
                        " ?," +                        //requestNumber       3
                        " ?," +                        //requestDate         4
                        " ?," +                        //clientID            5
                        " ?," +                        //destinationPointId  6
                        " ?," +                        //marketAgentUserId   7
                        " ?," +                        //invoiceNumber       8
                        " ?," +                        //invoiceDate         9
                        " ?," +                        //documentNumber      10
                        " ?," +                        //documentDate        11
                        " ?," +                        //firma               12
                        " ?," +                        //storage             13
                        " ?," +                        //contactName         14
                        " ?," +                        //contactPhone        15
                        " ?," +                        //deliveryOption      16
                        " ?," +                        //deliveryDate        17
                        " NOW()," +                    //lastStatusUpdated
                        " ?," +                        //lastModifiedBy      18
                        " 'CREATED'," +                //requestStatusId
                        " 'заявка добавлена из 1С'" +  //commentForStatus
                        " )\n" +
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
                        "  lastStatusUpdated       = VALUES(lastStatusUpdated),\n" +
                        "  lastModifiedBy          = VALUES(lastModifiedBy);"
        );

        for (RequestsData updateRequest : updateRequests) {
            requestsPreparedStatement.setString(1, updateRequest.getRequestId());
            requestsPreparedStatement.setString(2, DBManager.LOGIST_1C);
            requestsPreparedStatement.setString(3, updateRequest.getRequestNumber());
            requestsPreparedStatement.setDate  (4,  updateRequest.getRequestDate());
            try {
                setClientId(allClientsKeyMap, requestsPreparedStatement, 5, updateRequest.getClientId());
                setDestinationPoint(allPointsKeyMap, requestsPreparedStatement, updateRequest, 6, updateRequest.getAddressId());
                setMarketAgentUserId(allUsersKeyMap, requestsPreparedStatement, 7, updateRequest.getTraderId());
            } catch (DBCohesionException e) {
                logger.warn(e);
                continue;
            }
            requestsPreparedStatement.setString(8, updateRequest.getInvoiceNumber());
            requestsPreparedStatement.setDate  (9, updateRequest.getInvoiceDate());
            requestsPreparedStatement.setString(10, updateRequest.getDocumentNumber());
            requestsPreparedStatement.setDate  (11, updateRequest.getDocumentDate());
            requestsPreparedStatement.setString(12, updateRequest.getFirma());
            requestsPreparedStatement.setString(13, updateRequest.getStorage());
            requestsPreparedStatement.setString(14, updateRequest.getContactName());
            requestsPreparedStatement.setString(15, updateRequest.getContactPhone());
            requestsPreparedStatement.setString(16, updateRequest.getDeliveryOption());
            requestsPreparedStatement.setDate  (17, updateRequest.getDeliveryDate());
            requestsPreparedStatement.setInt   (18, parserUserId);
            requestsPreparedStatement.addBatch();
        }
        int[] requestsAffectedRecords = requestsPreparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE INTO requests completed, affected records size = [{}]", requestsAffectedRecords.length);
        return requestsPreparedStatement;
    }

    private void setClientId(BidiMap<String, Integer> allClientsKeyMap, PreparedStatement requestsPreparedStatement, int parameterIndex, String clientIdExternal) throws DBCohesionException, SQLException {
        Integer clientId = allClientsKeyMap.get(clientIdExternal);
        if (clientId == null)
            throw new DBCohesionException(this.getClass().getSimpleName(), RequestsData.FN_REQUEST_ID, RequestsData.FN_CLIENT_ID, clientIdExternal, "clients");
        else
            requestsPreparedStatement.setInt(parameterIndex, clientId);
    }

    private void setDestinationPoint(BidiMap<String, Integer> allPointsKeyMap, PreparedStatement requestsPreparedStatement, RequestsData updateRequest, int parameterIndex, String addressId) throws SQLException {
        Integer destinationPointId = allPointsKeyMap.get(addressId);
        if (destinationPointId == null) {
            logger.warn("[{}]: requestId [{}] has addressId = [{}] that is not contained in [points] table. Set NULL for pointID", this.getClass().getSimpleName(), updateRequest.getRequestId(), addressId);
            requestsPreparedStatement.setNull(parameterIndex, Types.INTEGER);
        } else
            requestsPreparedStatement.setInt(parameterIndex, destinationPointId);
    }

    private void setMarketAgentUserId(BidiMap<String, Integer> allUsersKeyMap, PreparedStatement requestsPreparedStatement, int parameterIndex, String traderId) throws DBCohesionException, SQLException {
        Integer marketAgentUserId = allUsersKeyMap.get(traderId);
        if (marketAgentUserId == null) {
            throw new DBCohesionException(this.getClass().getSimpleName(), RequestsData.FN_REQUEST_ID, RequestsData.FN_TRADER_ID, traderId, "users");
        } else {
            requestsPreparedStatement.setInt(parameterIndex, marketAgentUserId);
        }
    }

}
