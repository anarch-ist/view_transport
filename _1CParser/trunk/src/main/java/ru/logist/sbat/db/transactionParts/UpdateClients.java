package ru.logist.sbat.db.transactionParts;


import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.jsonParser.beans.ClientData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateClients extends TransactionPart{
    private List<ClientData> updateClients;

    public UpdateClients(List<ClientData> updateClients) {
        this.updateClients = updateClients;
    }

    @Override
    PreparedStatement executePart() throws SQLException {
        logger.info("-----------------START update clients table from JSON object:[clients]-----------------");
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN)\n" +
                        "  VALUE (?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  clientName = VALUES(clientName),\n" +
                        "  INN        = VALUES(INN);"
        );

        for (ClientData updateClient : updateClients) {
            preparedStatement.setString(1, updateClient.getClientId()); //clientIDExternal
            preparedStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            preparedStatement.setString(3, updateClient.getClientName());
            preparedStatement.setString(4, updateClient.getClientINN());
            preparedStatement.addBatch();
        }
        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE for clients completed, affected records size = [{}]", affectedRecords.length);

        return preparedStatement;
    }
}
