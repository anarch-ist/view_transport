package ru.logistica.tms;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestUtil {

    public static void jdbcRecreateDatabase() throws URISyntaxException, IOException, SQLException {
        Path pathToSql = Paths.get(TestUtil.class.getResource("ddl.sql").toURI());
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        SqlScriptRunner sqlScriptRunner = new SqlScriptRunner(connection, pathToSql, false);
        sqlScriptRunner.run();
        connection.close();
    }

    public static void jdbcRecreateAndTestInserts() throws URISyntaxException, SQLException, IOException {
        Path ddlPath = Paths.get(TestUtil.class.getResource("ddl.sql").toURI());
        Path testInsertsPath = Paths.get(TestUtil.class.getResource("test_inserts.sql").toURI());
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
        SqlScriptRunner ddlSqlScriptRunner = new SqlScriptRunner(connection, ddlPath, false);
        ddlSqlScriptRunner.run();
        SqlScriptRunner testInsertsSqlScriptRunner = new SqlScriptRunner(connection, testInsertsPath, false);
        testInsertsSqlScriptRunner.run();
        connection.close();
    }
}
