package ru.logist.sbat.fileExecutor;

import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.db.DBUtils;
import ru.logist.sbat.db.TransactionResult;
import ru.logist.sbat.jsonToBean.beans.DataFrom1c;
import ru.logist.sbat.resourcesInit.ResourceInitException;

import java.sql.SQLException;
import java.util.Objects;

public class BeanIntoDataBaseCmd {
    private DataFrom1c dataFrom1c;
    private DBManager dbManager;

    public BeanIntoDataBaseCmd(DataFrom1c dataFrom1c, DBManager dbManager) {
        this.dataFrom1c = dataFrom1c;
        this.dbManager = dbManager;
    }

    public void execute() throws DBCohesionException, SQLException, ResourceInitException {
        Objects.requireNonNull(dataFrom1c);
        Objects.requireNonNull(dbManager);
        dbManager.updateDataFromJSONObject(dataFrom1c);
    }
}
