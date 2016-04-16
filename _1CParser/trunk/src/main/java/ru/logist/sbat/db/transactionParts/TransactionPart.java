package ru.logist.sbat.db.transactionParts;

import java.sql.*;

public abstract class TransactionPart {
    protected Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public abstract Statement executePart() throws SQLException, DBCohesionException;
}
