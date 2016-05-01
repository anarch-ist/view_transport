package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.DBManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DeleteRouteLists extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private List<String> routeListIdExternals;

    public DeleteRouteLists(List<String> routeListIdExternals) {
        this.routeListIdExternals = routeListIdExternals;
    }

    @Override
    public Statement executePart() throws SQLException, DBCohesionException {
        if (routeListIdExternals.isEmpty())
            return null;

        logger.info("START DELETE FROM route_lists TABLE from JSON object:[deleteRouteLists]");
        PreparedStatement prepareStatement =  connection.prepareStatement(
                "DELETE FROM route_lists WHERE routeListIDExternal = ? AND dataSourceID = ?"
        );

        for (String routeListIdExternal : routeListIdExternals) {
            prepareStatement.setString(1, routeListIdExternal);
            prepareStatement.setString(2, DBManager.LOGIST_1C);
            prepareStatement.addBatch();
        }

        int[] affectedRecords = prepareStatement.executeBatch();
        logger.info("DELETE FROM route_lists completed, affected records size = [{}]", affectedRecords.length);
        return prepareStatement;
    }
}
