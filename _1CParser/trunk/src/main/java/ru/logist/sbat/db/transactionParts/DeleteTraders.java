package ru.logist.sbat.db.transactionParts;

import java.util.List;

public class DeleteTraders extends DeleteTransactionPart {

    public DeleteTraders(List<String> externalIdForDeleteList) {
        super(externalIdForDeleteList);
    }

    @Override
    public String getTableName() {
        return "users";
    }

    @Override
    public String getJsonObjectName() {
        return "deleteTrader";
    }

    @Override
    public String getExternalColumnName() {
        return "userIDExternal";
    }
}
