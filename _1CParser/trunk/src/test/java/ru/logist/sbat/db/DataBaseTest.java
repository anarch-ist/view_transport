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
//        URL ddl = JSONReadFromFileTest.class.getResource("tables_and_functions.sql");
//        List<String> strings = Files.readAllLines(Paths.get(ddl.toURI()), StandardCharsets.UTF_8);
//        StringBuilder stringBuilder = new StringBuilder();
//        for(String string: strings) {
//            string = string.replaceAll("--.+", "");
//            stringBuilder.append(string + "\n");
//        }
//        String sqlStringWithoutComments = stringBuilder.toString();
//        System.out.println(sqlStringWithoutComments);
        //String ddlAsString = new String(bytes, StandardCharsets.UTF_8);


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

        //dataBase.importSQL(new ByteArrayInputStream(sqlStringWithoutComments.getBytes("UTF-8")));
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