package ru.logistica.tms.dao.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnection {
    private InitialContext initialContext;
    private DataSource dataSource;

    public Connection getConnection() throws SQLException, NamingException, ClassNotFoundException {
        initialContext = new InitialContext();
        dataSource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/postgres");
        return dataSource.getConnection();
    }
}
