package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DeleteRequests extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private List<String> requestIdExternalList;

    public DeleteRequests(List<String> requestIdExternalList) {
        this.requestIdExternalList = requestIdExternalList;
    }

    @Override
    public Statement executePart() throws SQLException, DBCohesionException {
        if (requestIdExternalList.isEmpty())
            return null;

        logger.info("START DELETE FROM REQUESTS TABLE from JSON object:[deleteRequests]");
        PreparedStatement prepareStatement =  connection.prepareStatement(
                "DELETE FROM requests WHERE requestIDExternal = ? AND dataSourceID = ?"
        );

        for (String requestIdExternal : requestIdExternalList) {
            prepareStatement.setString(1, requestIdExternal);
            prepareStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            prepareStatement.addBatch();
        }

        int[] affectedRecords = prepareStatement.executeBatch();
        logger.info("DELETE FROM REQUESTS completed, affected records size = [{}]", affectedRecords.length);
        return prepareStatement;
    }
}
