package ru.logistica.tms.dao.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtils {
    public static void rollbackQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {/*NOPE*/}
        }
    }

    public static void closeConnectionQuietly(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {/*NOPE*/}
        }
    }

}
