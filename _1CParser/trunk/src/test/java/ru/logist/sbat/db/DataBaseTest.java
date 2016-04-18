package ru.logist.sbat.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.logist.sbat.resourcesInit.SystemResourcesContainer;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.jsonParser.JSONReadFromFileTest;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;
import ru.logist.sbat.resourcesInit.PropertiesPojo;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * This test working only if database launched
 */
public class DataBaseTest {

    public static DBManager DBManager;
    public static DataFrom1c dataFrom1c;
    private static final Logger logger = LogManager.getLogger();

    @BeforeClass
    public static void setUp() throws Exception {
    
        // get JSON object
        Path path = Paths.get(JSONReadFromFileTest.class.getResource("raw_data_18.04.2016/EKA.zip").toURI());
        dataFrom1c = JSONReadFromFile.getJsonObjectFromFile(path);

        // get connection to database
        Properties testProperties = new Properties();
        testProperties.loadFromXML(DataBaseTest.class.getResourceAsStream("test_config.property"));
        // testProperties.loadFromXML(DataBaseTest.class.getResourceAsStream("prod_config.property"));
        SystemResourcesContainer systemResourcesContainer = new SystemResourcesContainer(new PropertiesPojo(testProperties), logger);

        DBManager = new DBManager(systemResourcesContainer.getConnection());

        // clean database content
        DBManager.truncatePublicTables();
    }

    @Test
    public void testUpdateDataFromJSONObject() throws Exception {
        DBManager.updateDataFromJSONObject(dataFrom1c);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (DBManager != null)
            DBManager.close();
    }
}