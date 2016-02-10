package ru.logist.sbat.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    private static final Logger logger = LogManager.getLogger(DBUtils.class);

    public static void closeConnectionQuietly(Connection connection) {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public static void closeStatementQuietly(Statement statement) {
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public static void rollbackQuietly(Connection connection) {
        try {
            if (connection != null)
                connection.rollback();
        } catch (SQLException e) {
            logger.error(e);
        }
    }
}
