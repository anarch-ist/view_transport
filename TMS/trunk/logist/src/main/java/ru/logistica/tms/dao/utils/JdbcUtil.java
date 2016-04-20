package ru.logistica.tms.dao.utils;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;

public class JdbcUtil {
    static private Connection connection;

    public static void setConnection(Connection connection) {
        JdbcUtil.connection = connection;
        try {
            JdbcUtil.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void rollbackQuietly() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {/*NOPE*/}
        }
    }

    public static void runWithExceptionRedirect(Exec exec) throws DaoException {
        try {
            exec.execute();
        } catch (Exception e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    public interface Exec {
        void execute() throws Exception;
    }

}
