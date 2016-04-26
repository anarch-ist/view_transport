package ru.logistica.tms.dao.utils;


import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    static private Connection connection;

    public static void setConnection(Connection connection) {
        try {
            connection.setAutoCommit(false);
            ConnectionManager.connection = connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
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
