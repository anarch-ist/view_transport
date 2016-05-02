package ru.logist.sbat.fileExecutor;

import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.db.DBUtils;
import ru.logist.sbat.db.TransactionResult;
import ru.logist.sbat.jsonToBean.beans.DataFrom1c;

import java.sql.SQLException;
import java.util.Objects;

public class BeanIntoDataBaseCmd implements Command<TransactionResult> {
    private DataFrom1c dataFrom1c;
    private DBManager dbManager;

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void setDataFrom1c(DataFrom1c dataFrom1c) {
        this.dataFrom1c = dataFrom1c;
    }

    @Override
    public TransactionResult execute() throws CommandException {
        Objects.requireNonNull(dataFrom1c);
        Objects.requireNonNull(dbManager);
        try {
            return dbManager.updateDataFromJSONObject(dataFrom1c);
        } catch (DBCohesionException|SQLException e) {
            DBUtils.rollbackQuietly(dbManager.getConnection());
            throw new CommandException(e);
        }
    }
}
