package ru.logist.sbat.db.transactionParts;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.GlobalUtils;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.db.DBUtils;
import ru.logist.sbat.db.Utils;
import ru.logist.sbat.jsonToBean.beans.TraderData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

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
        //2016-07-07 16:23:35,279 ERROR ru.logist.sbat.fileExecutor.CommandsExecutor [pool-1-thread-1] java.sql.BatchUpdateException: Duplicate entry 'bogomolov@ryazan.sbat.ru' for key 'login'
        logger.info("START update users table from JSON object:[updateTrader]");
        Set<String> allUserLogins = Selects.getInstance().allUserLogins();
        PreparedStatement result = connection.prepareStatement(
                "INSERT INTO users (userIDExternal, dataSourceID, login, salt, passAndSalt, userRoleID, userName, phoneNumber, email, position)\n" +
                        "  VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  login       = VALUES(login),\n" +
                        "  salt        = VALUES(salt),\n" +
                        "  passAndSalt = VALUES(passAndSalt),\n" +
                        "  userRoleID  = VALUES(userRoleID),\n" +
                        "  userName    = VALUES(userName),\n" +
                        "  phoneNumber = VALUES(phoneNumber),\n" +
                        "  email       = VALUES(email),\n" +
                        "  position    = VALUES(position);"
        );

        for (TraderData updateUser : updateMarketAgents) {
            result.setString(1, updateUser.getTraderId());
            result.setString(2, DBManager.LOGIST_1C);
            if (updateUser.hasValidLoginAndPassword()) {
                String salt = Utils.generateSalt();
                String login = updateUser.getTraderLogin();
                if (allUserLogins.contains(login)) {
                    logger.warn(GlobalUtils.getParameterizedString("Can't insert or update user {}, login {} already exist in {} table",  updateUser.getTraderId(), login, "users"));
                    continue;
                }
                result.setString(3, login);
                result.setString(4, salt);
                result.setString(5, Utils.generatePassAndSalt(updateUser.getTraderPassword(), salt));
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
