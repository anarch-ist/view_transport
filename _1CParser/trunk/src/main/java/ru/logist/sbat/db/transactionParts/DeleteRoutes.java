package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DeleteRoutes extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private List<String> directionIdExternals;

    public DeleteRoutes(List<String> directionIdExternals) {
        this.directionIdExternals = directionIdExternals;
    }

    @Override
    public Statement executePart() throws SQLException {
        logger.warn("directions are never remove");
        return null; /*DO NOTHING*/
    }
}
