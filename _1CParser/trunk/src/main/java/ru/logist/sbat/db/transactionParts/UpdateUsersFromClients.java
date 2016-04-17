package ru.logist.sbat.db.transactionParts;


import org.apache.commons.collections4.BidiMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.db.Utils;
import ru.logist.sbat.jsonParser.beans.ClientData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateUsersFromClients extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private static final String CLIENT_MANAGER = "CLIENT_MANAGER";
    public static final String CLIENT_USER_SUFFIX = "-client";
    private List<ClientData> updateClients;

    public UpdateUsersFromClients(List<ClientData> updateClients) {
        this.updateClients = updateClients;
    }

    @Override
    public PreparedStatement executePart() throws SQLException, DBCohesionException {
        if (updateClients.isEmpty())
            return null;
        BidiMap<String, Integer> allClientsAsKeyPairs = Selects.allClientsAsKeyPairs();
        logger.info("-----------------START update users table from JSON object:[updateTrader]-----------------");
        PreparedStatement result = connection.prepareStatement(
                "INSERT INTO users (userIDExternal, dataSourceID, login, salt, passAndSalt, userRoleID, userName, clientID)\n" +
                        "  VALUE (?, ?, ?, ?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  login  = VALUES(login),\n" +
                        "  salt        = VALUES(salt),\n" +
                        "  passAndSalt = VALUES(passAndSalt),\n" +
                        "  userRoleID = VALUES(userRoleID),\n" +
                        "  userName    = VALUES(userName),\n" +
                        "  clientID  = VALUES(clientID);\n");

        for (ClientData updateClient : updateClients) {
            // нужно чтобы избежать возникновения одинаковых userIdExternal(никто не гарантирует, что clientIdExternal и userIdExternal всегда разные)
            String userIdExternal = updateClient.getClientId() + CLIENT_USER_SUFFIX;
            String login = updateClient.getClientId();

            result.setString(1, userIdExternal);
            result.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            if (updateClient.hasValidPassword()) {
                String salt = Utils.generateSalt();
                result.setString(3, login);
                result.setString(4, salt);
                result.setString(5, Utils.generatePassword(updateClient.getClientPassword(), salt));
            }
            else {
                // random values
                result.setString(3, RandomStringUtils.randomAscii(16));
                result.setString(4, Utils.generateSalt());
                result.setString(5, RandomStringUtils.randomAscii(16));
            }

            result.setString(6, CLIENT_MANAGER);
            result.setString(7, updateClient.getClientName());
            setClientId(allClientsAsKeyPairs, result, 8, updateClient.getClientId());

            result.addBatch();
        }

        int[] insertedOrUpdateRecords = result.executeBatch();
        logger.info("INSERT OR UPDATE INTO users completed, affected records size = [{}]", insertedOrUpdateRecords.length);
        return result;
    }

    private void setClientId(BidiMap<String, Integer> allClientsAsKeyPairs, PreparedStatement result, int parameterIndex, String clientIdExternal) throws DBCohesionException, SQLException {
        Integer clientId = allClientsAsKeyPairs.get(clientIdExternal);
        if (clientId == null) {
            throw new DBCohesionException(UpdateUsersFromClients.class.getSimpleName(), ClientData.FN_CLIENT_ID, ClientData.FN_CLIENT_ID, clientIdExternal, "clients");
        }else {
            result.setInt(parameterIndex, clientId);
        }
    }
}
