package ru.logist.sbat.db;

import org.json.simple.JSONObject;
import org.junit.*;
import ru.logist.sbat.db.DataBase;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.jsonParser.JSONReadFromFileTest;
import ru.logist.sbat.properties.DefaultProperties;

import java.io.InputStream;
import java.net.URL;

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
        DefaultProperties defaultProperties = new DefaultProperties();
        dataBase = new DataBase(
                defaultProperties.getProperty("url"),
                defaultProperties.getProperty("dbName"),
                defaultProperties.getProperty("user"),
                defaultProperties.getProperty("password"),
                defaultProperties.getProperty("encoding")
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