package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.jsonParser.beans.AddressData;
import ru.logist.sbat.jsonParser.beans.PointData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdatePointsFromAddresses extends TransactionPart {
    private static final Logger logger = LogManager.getLogger();
    private final List<AddressData> updateAddresses;

    public UpdatePointsFromAddresses(List<AddressData> updateAddresses) {
        this.updateAddresses = updateAddresses;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        if (updateAddresses.isEmpty())
            return null;

        logger.info("START update points table from JSON objects: [updateAddresses]");

        PreparedStatement pointsStatement =  connection.prepareStatement(
                "INSERT INTO points (pointIDExternal, dataSourceID, pointName, address, pointTypeID)\n" +
                        "VALUE (?, ?, ?, ?, ?)\n" +
                        "ON DUPLICATE KEY UPDATE\n" +
                        "  pointName           = VALUES(pointName),\n" +
                        "  address             = VALUES(address),\n" +
                        "  pointTypeID         = VALUES(pointTypeID);"
        );

        // create or update points from updateAddresses JSON array
        for (AddressData updateAddress : updateAddresses) {
            String pointIdExternal = updateAddress.getAddressId();
            String pointName = updateAddress.getAddressShot();
            String pointAddress = updateAddress.getAddressFull();

            pointsStatement.setString(1, pointIdExternal);
            pointsStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            pointsStatement.setString(3, pointName);
            pointsStatement.setString(4, pointAddress);
            pointsStatement.setString(5, "AGENCY");
            pointsStatement.addBatch();
        }

        int[] affectedRecords = pointsStatement.executeBatch();
        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [points] completed, affected records size = [{}]", affectedRecords.length);
        return pointsStatement;
    }
}
