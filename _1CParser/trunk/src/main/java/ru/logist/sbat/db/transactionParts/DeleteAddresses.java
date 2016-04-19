package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DeleteAddresses extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private List<String> addressIdExternalsList;

    public DeleteAddresses(List<String> addressIdExternalsList) {
        this.addressIdExternalsList = addressIdExternalsList;
    }

    @Override
    public Statement executePart() throws SQLException, DBCohesionException {
        logger.warn("points are never remove");
        return null; /*DO NOTHING*/
    }
}