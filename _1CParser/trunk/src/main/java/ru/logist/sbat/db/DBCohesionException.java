package ru.logist.sbat.db;

import ru.logist.sbat.GlobalUtils;

public class DBCohesionException extends Exception {
    public DBCohesionException(String message) {
        super(message);
    }

    public DBCohesionException(String transactionPart, String rootParameterName, String parameterName, String parameterValue, String tableName) {
        super(GlobalUtils.getParameterizedString(
                transactionPart + ": {} has {} = {} that is not contained in {} table.",
                rootParameterName,
                parameterName,
                parameterValue,
                tableName
        ));
    }
}
