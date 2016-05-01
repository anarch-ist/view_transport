package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.jsonParser.beans.PointData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdatePoints extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private final List<PointData> updatePointsArray;

    public UpdatePoints(List<PointData> updatePointsArray) {
        this.updatePointsArray = updatePointsArray;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updatePointsArray.isEmpty())
            return null;

        logger.info("START update points table from JSON object:[updatePointsArray]");

        PreparedStatement pointsStatement =  connection.prepareStatement(
                "INSERT INTO points (pointIDExternal, dataSourceID, pointName, address, email, pointTypeID, responsiblePersonId)\n" +
                        "VALUE (?, ?, ?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  pointName           = VALUES(pointName),\n" +
                        "  address             = VALUES(address),\n" +
                        "  email               = VALUES(email),\n" +
                        "  pointTypeID         = VALUES(pointTypeID),\n" +
                        "  responsiblePersonId = VALUES(responsiblePersonId);"
        );

        // create or update points from updatePoints JSON array
        for (PointData updatePoint : updatePointsArray) {
            pointsStatement.setString(1, updatePoint.getPointId());
            pointsStatement.setString(2, DBManager.LOGIST_1C);
            pointsStatement.setString(3, updatePoint.getPointName());
            pointsStatement.setString(4, updatePoint.getPointAddress());
            pointsStatement.setString(5, updatePoint.getPointEmails());
            pointsStatement.setString(6, updatePoint.getPointType());
            pointsStatement.setString(7, updatePoint.getResponsiblePersonId());
            pointsStatement.addBatch();
        }

        int[] affectedRecords = pointsStatement.executeBatch();
        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [points] completed, affected records size = [{}]", affectedRecords.length);
        return pointsStatement;
    }
}
