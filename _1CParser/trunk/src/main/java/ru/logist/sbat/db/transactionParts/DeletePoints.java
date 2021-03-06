package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DeletePoints extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private List<String> pointsIdExternalsList;

    public DeletePoints(List<String> pointsIdExternalsList) {
        this.pointsIdExternalsList = pointsIdExternalsList;
    }

    @Override
    public Statement executePart() throws SQLException, DBCohesionException {
        logger.warn("points are never remove");
        return null; /*DO NOTHING*/
    }
}
