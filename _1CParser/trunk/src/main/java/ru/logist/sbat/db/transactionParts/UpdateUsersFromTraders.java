package ru.logist.sbat.db.transactionParts;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.db.Utils;
import ru.logist.sbat.jsonParser.beans.ClientData;
import ru.logist.sbat.jsonParser.beans.TraderData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateUsersFromTraders extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private static final String MARKET_AGENT = "MARKET_AGENT";
    private List<TraderData> updateMarketAgents;

    public UpdateUsersFromTraders(List<TraderData> updateMarketAgents) {
        this.updateMarketAgents = updateMarketAgents;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateMarketAgents.isEmpty())
            return null;

        logger.info("START update users table from JSON object:[updateTrader]");
        PreparedStatement result = connection.prepareStatement(
                "INSERT INTO users (userIDExternal, dataSourceID, login, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position)\n" +
                        "  VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  login  = VALUES(login),\n" +
                        "  salt        = VALUES(salt),\n" +
                        "  passAndSalt = VALUES(passAndSalt),\n" +
                        "  userRoleID = VALUES(userRoleID),\n" +
                        "  userName    = VALUES(userName),\n" +
                        "  phoneNumber = VALUES(phoneNumber),\n" +
                        "  email       = VALUES(email),\n" +
                        "  position    = VALUES(position);"
        );

        for (TraderData updateUser : updateMarketAgents) {
            result.setString(1, updateUser.getTraderId());
            result.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            if (updateUser.hasValidLoginAndPassword()) {
                String salt = Utils.generateSalt();
                result.setString(3, updateUser.getTraderLogin());
                result.setString(4, salt);
                result.setString(5, Utils.generatePassword(updateUser.getTraderPassword(), salt));
            }
            else {
                // random values
                result.setString(3, RandomStringUtils.randomAscii(16));
                result.setString(4, Utils.generateSalt());
                result.setString(5, RandomStringUtils.randomAscii(16));
            }
            result.setString(6, MARKET_AGENT);
            result.setString(7, updateUser.getTraderName());
            result.setString(8, updateUser.getTraderPhone());
            result.setString(9, updateUser.getTraderEmails());
            result.setString(10, updateUser.getTraderOffice()); // position

            result.addBatch();
        }
        int[] insertedOrUpdateRecords = result.executeBatch();
        logger.info("INSERT OR UPDATE INTO users completed, affected records size = [{}]", insertedOrUpdateRecords.length);
        return result;
    }
}
