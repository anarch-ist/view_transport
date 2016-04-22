package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DeleteTraders  extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private List<String> traderIdExternals;

    public DeleteTraders(List<String> traderIdExternals) {
        this.traderIdExternals = traderIdExternals;
    }

    @Override
    public Statement executePart() throws SQLException, DBCohesionException {
        if (traderIdExternals.isEmpty())
            return null;

        logger.info("START UPDATE USERS TABLE from JSON object:[deleteTraders]");
        PreparedStatement prepareStatement =  connection.prepareStatement(
                "UPDATE users SET userRoleID = 'TEMP_REMOVED' WHERE login = ?"
        );

        for (String traderIdExternal : traderIdExternals) {
            prepareStatement.setString(1, traderIdExternal);
            prepareStatement.addBatch();
        }

        int[] affectedRecords = prepareStatement.executeBatch();
        logger.info("UPDATE USERS completed, affected records size = [{}]", affectedRecords.length);
        return prepareStatement;
    }
}
