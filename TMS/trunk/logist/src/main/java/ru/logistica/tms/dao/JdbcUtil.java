package ru.logistica.tms.dao;


import java.sql.Connection;
import java.sql.SQLException;

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

    public static void rollbackQuietly() throws SQLException {
        try {
            getConnection().rollback();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
