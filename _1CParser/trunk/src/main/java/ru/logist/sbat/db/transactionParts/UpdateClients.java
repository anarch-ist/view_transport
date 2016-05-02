package ru.logist.sbat.db.transactionParts;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.jsonToBean.beans.ClientData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateClients extends TransactionPart{
    private static final Logger logger = LogManager.getLogger();
    private List<ClientData> updateClients;

    public UpdateClients(List<ClientData> updateClients) {
        this.updateClients = updateClients;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateClients.isEmpty())
            return null;

        logger.info("START update clients table from JSON object:[clients]");
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO clients (clientIDExternal, dataSourceID, clientName, INN)\n" +
                        "  VALUE (?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  clientName = VALUES(clientName),\n" +
                        "  INN        = VALUES(INN);"
        );

        for (ClientData updateClient : updateClients) {
            preparedStatement.setString(1, updateClient.getClientId()); //clientIDExternal
            preparedStatement.setString(2, DBManager.LOGIST_1C);
            preparedStatement.setString(3, updateClient.getClientName());
            preparedStatement.setString(4, updateClient.getClientINN());
            preparedStatement.addBatch();
        }
        int[] affectedRecords = preparedStatement.executeBatch();
        logger.info("INSERT OR UPDATE for clients completed, affected records size = [{}]", affectedRecords.length);

        return preparedStatement;
    }
}
