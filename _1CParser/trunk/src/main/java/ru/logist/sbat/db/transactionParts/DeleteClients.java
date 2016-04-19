package ru.logist.sbat.db.transactionParts;

import java.util.List;

public class DeleteClients extends DeleteTransactionPart {
    public DeleteClients(List<String> externalIdForDeleteList) {
        super(externalIdForDeleteList);
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String getJsonObjectName() {
        return null;
    }

    @Override
    public String getExternalColumnName() {
        return null;
    }
}
