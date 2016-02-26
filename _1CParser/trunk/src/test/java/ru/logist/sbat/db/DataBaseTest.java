package ru.logist.sbat.db;

import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.jsonParser.JSONReadFromFileTest;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * This test working only if database launched
 */
public class DataBaseTest {

    public static DataBase dataBase;
    public static JSONObject jsonObject;

    @BeforeClass
    public static void setUp() throws Exception {

        // get JSON object
        URL resource = JSONReadFromFileTest.class.getResource("EKA_fixed.pkg");
        InputStream inputStream = resource.openStream();
        jsonObject = JSONReadFromFile.read(inputStream);

        // get connection to database
        Properties testProperties = new Properties();
        testProperties.loadFromXML(DataBaseTest.class.getResourceAsStream("test_config.property"));
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
        dataBase.updateDataFromJSONObject(jsonObject);
    }



}