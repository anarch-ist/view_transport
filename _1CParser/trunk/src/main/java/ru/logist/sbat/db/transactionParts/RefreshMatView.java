package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class RefreshMatView extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Statement executePart() throws SQLException, DBCohesionException {
        logger.info("--------------START refresh materialized view------------------");
        CallableStatement callableStatement = connection.prepareCall("{CALL refreshMaterializedView()}");
        callableStatement.execute();
        logger.info("--------------END refresh materialized view------------------");
        return callableStatement;
    }
}
