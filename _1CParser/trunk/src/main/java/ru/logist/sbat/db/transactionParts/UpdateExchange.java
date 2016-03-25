package ru.logist.sbat.db.transactionParts;


import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateExchange extends TransactionPart {
    private DataFrom1c dataFrom1c;

    public UpdateExchange(DataFrom1c dataFrom1c) {
        this.dataFrom1c = dataFrom1c;
    }

    @Override
    PreparedStatement executePart() throws SQLException {
        logger.info("-----------------START update exchange table from JSON object:[dataFrom1C]-----------------");
        PreparedStatement preparedStatement =  connection.prepareStatement(
                "INSERT INTO exchange (packageNumber, serverName, dataSource, packageCreated, packageData)\n" +
                        "VALUE (?, ?, ?, ?, ?);"
        );
        preparedStatement.setInt   (1, dataFrom1c.getPackageNumber().intValue());
        preparedStatement.setString(2, dataFrom1c.getServer());
        preparedStatement.setString(3, InsertOrUpdateTransactionScript.LOGIST_1C);
        preparedStatement.setDate  (4, dataFrom1c.getCreated());
        preparedStatement.setString(5, dataFrom1c.getRawJsonObject());
        preparedStatement.executeUpdate();
        logger.info("INSERT INTO exchange table completed packageNumber = [{}]", dataFrom1c.getPackageNumber().intValue());
        return preparedStatement;
    }
}
