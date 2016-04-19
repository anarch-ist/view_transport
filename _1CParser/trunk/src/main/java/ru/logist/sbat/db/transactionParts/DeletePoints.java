package ru.logist.sbat.db.transactionParts;

import java.util.List;

public class DeletePoints extends DeleteTransactionPart {

    public DeletePoints(List<String> externalIdForDeleteList) {
        super(externalIdForDeleteList);
    }

    @Override
    public String getTableName() {
        return "points";
    }

    @Override
    public String getJsonObjectName() {
        return "deletePoints";
    }

    @Override
    public String getExternalColumnName() {
        return "pointIDExternal";
    }
}
