package ru.logist.sbat.db.transactionParts;

import ru.logist.sbat.db.InsertOrUpdateTransactionScript;
import ru.logist.sbat.db.Utils;
import ru.logist.sbat.jsonParser.beans.AddressData;
import ru.logist.sbat.jsonParser.beans.PointData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdatePoints extends TransactionPart {
    private final List<PointData> updatePointsArray;
    private final List<AddressData> updateAddresses;

    public UpdatePoints(List<PointData> updatePointsArray, List<AddressData> updateAddresses) {
        this.updatePointsArray = updatePointsArray;
        this.updateAddresses = updateAddresses;
    }

    @Override
    public PreparedStatement executePart() throws SQLException {
        logger.info("-----------------START update points table from JSON objects:[updatePointsArray], [updateAddresses]-----------------");

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
            pointsStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            pointsStatement.setString(3, updatePoint.getPointName());
            pointsStatement.setString(4, updatePoint.getPointAddress());
            pointsStatement.setString(5, updatePoint.getPointEmails());
            pointsStatement.setString(6, updatePoint.getPointType());
            pointsStatement.setString(7, updatePoint.getResponsiblePersonId());
            pointsStatement.addBatch();
        }

        // create or update points from updateAddresses JSON array
        // add only unique points
        Utils.UniqueCheck uniqueCheckAddressId = Utils.getUniqueCheckObject("addressId");
        for (AddressData updateAddress : updateAddresses) {
            String pointIdExternal = updateAddress.getAddressId();
            if (!uniqueCheckAddressId.isUnique(pointIdExternal))
                continue;

            String pointName = updateAddress.getAddressShot();
            String pointAddress = updateAddress.getAddressFull();

            pointsStatement.setString(1, pointIdExternal);
            pointsStatement.setString(2, InsertOrUpdateTransactionScript.LOGIST_1C);
            pointsStatement.setString(3, pointName);
            pointsStatement.setString(4, pointAddress);
            pointsStatement.setString(5, "");
            pointsStatement.setString(6, "AGENCY");
            pointsStatement.setString(7, "");
            pointsStatement.addBatch();
        }

        int[] affectedRecords = pointsStatement.executeBatch();
        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [points] completed, affected records size = [{}]", affectedRecords.length);
        return pointsStatement;
    }
}
