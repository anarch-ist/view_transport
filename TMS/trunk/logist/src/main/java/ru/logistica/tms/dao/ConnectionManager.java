package ru.logistica.tms.dao;


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

}
