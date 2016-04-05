package ru.logist.sbat.testDataGenerator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.logist.sbat.db.DataBase;
import ru.logist.sbat.db.DataBaseTest;

import java.util.Properties;

public class DataInserterTest {

    private DataInserter dataInserter;

    @Before
    public void setUp() throws Exception {

        // get connection to database
        Properties testProperties = new Properties();
        testProperties.loadFromXML(DataBaseTest.class.getResourceAsStream("test_config.property"));

        DataBase dataBase = new DataBase(
                testProperties.getProperty("url"),
                testProperties.getProperty("dbName"),
                testProperties.getProperty("user"),
                testProperties.getProperty("password"),
                testProperties.getProperty("encoding")
        );
        dataInserter = new DataInserter(dataBase);
        // dataBase.truncatePublicTables();

    }

    @Ignore
    @Test
    public void testGenerate() throws Exception {
        dataInserter.generatePoints();
        dataInserter.generateRoutes();
        dataInserter.generateClients();
        dataInserter.generateUsers();
        dataInserter.generateRouteLists();
        dataInserter.generateRequests();
        dataInserter.refreshView();
    }


    @Test
    public void testCreateIndex() throws Exception {
        dataInserter.createIndex();
    }
}
