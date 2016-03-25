package ru.logist.sbat.db.transactionParts;

import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class TransactionPart {
    protected static Connection connection;
    protected static Logger logger;

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        TransactionPart.connection = connection;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        TransactionPart.logger = logger;
    }

    abstract PreparedStatement executePart() throws SQLException;
}
