package ru.logist.sbat.db.transactionParts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class TransactionPart {
    protected Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public abstract PreparedStatement executePart() throws SQLException, DBCohesionException;
}
