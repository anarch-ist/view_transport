package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.db.Utils;
import ru.logist.sbat.jsonParser.Util;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateExchange extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private DataFrom1c dataFrom1c;

    public UpdateExchange(DataFrom1c dataFrom1c) {
        this.dataFrom1c = dataFrom1c;
    }

    @Override
    public PreparedStatement executePart() throws SQLException, DBCohesionException {
        logger.info("START update exchange table from JSON object:[dataFrom1C]");
        BidiMap<String, Integer> allExchangeKeys = Selects.getInstance().allExchangeKeys();
        int packageNumber = dataFrom1c.getPackageNumber().intValue();
        String server = dataFrom1c.getServer();
        Integer lastReceivedPackageNumber = allExchangeKeys.get(server);
        if (lastReceivedPackageNumber != null && packageNumber <= lastReceivedPackageNumber)
            throw new DBCohesionException(Util.getParameterizedString("package number {} for {} server must be greater then {}", packageNumber, server, lastReceivedPackageNumber));

        PreparedStatement preparedStatement =  connection.prepareStatement(
                "INSERT INTO exchange (packageNumber, serverName, dataSourceID, packageCreated, packageData)\n" +
                        "VALUE (?, ?, ?, ?, ?);"
        );
        preparedStatement.setInt   (1, packageNumber);
        preparedStatement.setString(2, server);
        preparedStatement.setString(3, InsertOrUpdateTransactionScript.LOGIST_1C);
        preparedStatement.setTimestamp(4, dataFrom1c.getCreated());
        preparedStatement.setString(5, dataFrom1c.getRawJsonObject());
        preparedStatement.executeUpdate();
        logger.info("INSERT INTO exchange table completed server = [{}], packageNumber = [{}]", server, packageNumber);
        return preparedStatement;
    }
}
