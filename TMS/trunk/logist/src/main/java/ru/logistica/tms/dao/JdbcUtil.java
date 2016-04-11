package ru.logistica.tms.dao;


import java.sql.Connection;

public class JdbcUtil {
    static private Connection connection;

    public static void setConnection(Connection connection) {
        JdbcUtil.connection = connection;
    }

    public static Connection getConnection(){
        return connection;
    }
}
