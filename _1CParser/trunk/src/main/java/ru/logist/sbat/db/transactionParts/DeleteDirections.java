package ru.logist.sbat.db.transactionParts;

import java.util.List;

public class DeleteDirections extends DeleteTransactionPart {

    public DeleteDirections(List<String> externalIdForDeleteList) {
        super(externalIdForDeleteList);
    }

    @Override
    public String getTableName() {
        return "routes";
    }

    @Override
    public String getJsonObjectName() {
        return "deleteDirections";
    }

    @Override
    public String getExternalColumnName() {
        return "directionIDExternal";
    }
}
