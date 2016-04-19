package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.jsonParser.Util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class DeleteTransactionPart extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private List<String> externalIdForDeleteList;

    public DeleteTransactionPart(List<String> externalIdForDeleteList) {
        this.externalIdForDeleteList = externalIdForDeleteList;
    }

    public abstract String getTableName();
    public abstract String getJsonObjectName();
    public abstract String getExternalColumnName();


    @Override
    public Statement executePart() throws SQLException, DBCohesionException {
        if (externalIdForDeleteList.isEmpty())
            return null;

        logger.info(Util.getParameterizedString("START delete from {} table from JSON object: {}", getTableName(), getJsonObjectName()));

        PreparedStatement deletePrStatement =  connection.prepareStatement(
                "DELETE FROM " + getTableName() + " WHERE " + getExternalColumnName() + " = ? AND dataSourceID = ?;"
        );

        for (String externalIdForDelete : externalIdForDeleteList) {
            deletePrStatement.setString(1, externalIdForDelete);
            deletePrStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            deletePrStatement.addBatch();
        }

        int[] affectedRecords = deletePrStatement.executeBatch();
        logger.info(Util.getParameterizedString("DELETE FROM {} completed, affected records size = {}", getTableName(), affectedRecords.length));
        return deletePrStatement;
    }
}
