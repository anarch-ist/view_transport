package ru.logist.sbat.db;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.jsonParser.JSONReadFromFileTest;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * This test working only if database launched
 */
public class DataBaseTest {

    public static DataBase dataBase;
    public static DataFrom1c dataFrom1c;

    @BeforeClass
    public static void setUp() throws Exception {

        // get JSON object
        Path path = Paths.get(JSONReadFromFileTest.class.getResource("EKA_third.pkg").toURI());
        dataFrom1c = JSONReadFromFile.read(path);

        // get connection to database
        Properties testProperties = new Properties();
        //testProperties.loadFromXML(DataBaseTest.class.getResourceAsStream("test_config.property"));
        testProperties.loadFromXML(DataBaseTest.class.getResourceAsStream("prod_config.property"));
        dataBase = new DataBase(
                testProperties.getProperty("url"),
                testProperties.getProperty("dbName"),
                testProperties.getProperty("user"),
                testProperties.getProperty("password"),
                testProperties.getProperty("encoding")
        );

        // clean dataBase content
        dataBase.truncatePublicTables();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        dataBase.close();
    }

    @Test
    public void testUpdateDataFromJSONObject() throws Exception {
        dataBase.updateDataFromJSONObject(dataFrom1c);
    }



}