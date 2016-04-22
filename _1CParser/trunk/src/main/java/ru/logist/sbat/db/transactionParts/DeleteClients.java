package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DeleteClients extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private List<String> clientIdExternalList;

    public DeleteClients(List<String> clientIdExternalList) {
        this.clientIdExternalList = clientIdExternalList;
    }

    @Override
    public Statement executePart() throws SQLException, DBCohesionException {
        if (clientIdExternalList.isEmpty())
            return null;

        logger.info("START UPDATE USERS TABLE from JSON object:[deleteClients]");
        PreparedStatement prepareStatement =  connection.prepareStatement(
                "UPDATE users SET userRoleID = 'TEMP_REMOVED' WHERE login = ?"
        );

        for (String clientIdExternal : clientIdExternalList) {
            String clientUserLogin = clientIdExternal + UpdateUsersFromClients.CLIENT_USER_SUFFIX;
            prepareStatement.setString(1, clientUserLogin);
            prepareStatement.addBatch();
        }

        int[] affectedRecords = prepareStatement.executeBatch();
        logger.info("UPDATE USERS completed, affected records size = [{}]", affectedRecords.length);
        return prepareStatement;
    }
}
