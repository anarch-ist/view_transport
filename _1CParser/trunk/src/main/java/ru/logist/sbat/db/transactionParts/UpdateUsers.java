package ru.logist.sbat.db.transactionParts;


import org.apache.commons.lang3.RandomStringUtils;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.db.Utils;
import ru.logist.sbat.jsonParser.beans.ClientData;
import ru.logist.sbat.jsonParser.beans.TraderData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateUsers extends TransactionPart{
    private static final String MARKET_AGENT = "MARKET_AGENT";
    private static final String CLIENT_MANAGER = "CLIENT_MANAGER";
    public static final String CLIENT_USER_SUFFIX = "-client";
    private List<TraderData> updateMarketAgents;
    private List<ClientData> updateClients;

    public UpdateUsers(List<TraderData> updateMarketAgents, List<ClientData> updateClients) {
        this.updateMarketAgents = updateMarketAgents;
        this.updateClients = updateClients;
    }

    @Override
    PreparedStatement executePart() throws SQLException {
        logger.info("-----------------START update users table from JSON object:[updateTrader]-----------------");
        PreparedStatement result = connection.prepareStatement(
                "INSERT INTO users (userIDExternal, dataSourceID, login, passHash, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position, pointID, clientID)\n" +
                        "  VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, getClientIDByClientIDExternal(?, ?))\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  login  = VALUES(login),\n" +
                        "  passHash  = VALUES(passHash),\n" +
                        "  salt        = VALUES(salt),\n" +
                        "  passAndSalt = VALUES(passAndSalt),\n" +
                        "  userRoleID = VALUES(userRoleID),\n" +
                        "  userName    = VALUES(userName),\n" +
                        "  phoneNumber = VALUES(phoneNumber),\n" +
                        "  email       = VALUES(email),\n" +
                        "  position    = VALUES(position),\n" +
                        "  pointID    = VALUES(pointID),\n" +
                        "  clientID  = VALUES(clientID);\n");

        for (TraderData updateUser : updateMarketAgents) {
            result.setString(1, updateUser.getTraderId());
            result.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            result.setString(7, MARKET_AGENT);
            result.setString(8, updateUser.getTraderName());
            result.setString(9, updateUser.getTraderPhone());
            result.setString(10, updateUser.getTraderEmails());
            result.setString(11, updateUser.getTraderOffice()); // position
            result.setString(12, null); // pointID
            result.setString(13, null); // clientID
            result.setString(14, InsertOrUpdateTransactionScript.LOGIST_1C);

            if (updateUser.hasValidLoginAndPassword()) {
                String salt = Utils.generateSalt();
                result.setString(3, updateUser.getTraderLogin());
                result.setString(4, Utils.md5(updateUser.getTraderPassword()));
                result.setString(5, salt);
                result.setString(6, Utils.generatePassword(updateUser.getTraderPassword(), salt));
            }
            else {
                // random values
                result.setString(3, RandomStringUtils.randomAscii(16));
                result.setString(4, RandomStringUtils.randomAscii(16));
                result.setString(5, Utils.generateSalt());
                result.setString(6, RandomStringUtils.randomAscii(16));
            }
            result.addBatch();
        }

        for (ClientData updateClient : updateClients) {

            // insert into users
            String userIdExternal = updateClient.getClientId() + CLIENT_USER_SUFFIX;
            String login = updateClient.getClientId();
            result.setString(1, userIdExternal);
            result.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            result.setString(7, CLIENT_MANAGER);
            result.setString(8, updateClient.getClientName());
            result.setString(9, null);
            result.setString(10, null);
            result.setString(11, null); // position
            result.setString(12, null); // pointID
            result.setString(13, updateClient.getClientId()); // clientID - foreign key
            result.setString(14, InsertOrUpdateTransactionScript.LOGIST_1C);

            if (updateClient.hasValidPassword()) {
                String salt = Utils.generateSalt();
                result.setString(3, login);
                result.setString(4, Utils.md5(updateClient.getClientPassword()));
                result.setString(5, salt);
                result.setString(6, Utils.generatePassword(updateClient.getClientPassword(), salt));
            }
            else {
                // random values
                result.setString(3, RandomStringUtils.randomAscii(16));
                result.setString(4, RandomStringUtils.randomAscii(16));
                result.setString(5, Utils.generateSalt());
                result.setString(6, RandomStringUtils.randomAscii(16));
            }
            result.addBatch();
        }

        int[] insertedOrUpdateRecords = result.executeBatch();
        logger.info("INSERT OR UPDATE INTO users completed, affected records size = [{}]", insertedOrUpdateRecords.length);
        return result;
    }
}
