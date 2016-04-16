package ru.logist.sbat.testDataGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.resourcesInit.SystemResourcesContainer;
import ru.logist.sbat.db.DataBaseTest;
import ru.logist.sbat.resourcesInit.PropertiesPojo;

import java.util.Properties;

public class DataInserterTest {
    private static final Logger logger = LogManager.getLogger();
    private DataInserter dataInserter;

    @Before
    public void setUp() throws Exception {

        // get connection to database
        Properties testProperties = new Properties();
        testProperties.loadFromXML(DataBaseTest.class.getResourceAsStream("test_config.property"));
        SystemResourcesContainer systemResourcesContainer = new SystemResourcesContainer(new PropertiesPojo(testProperties), logger);
        DBManager DBManager = new DBManager(systemResourcesContainer.getConnection());
        dataInserter = new DataInserter(systemResourcesContainer.getConnection());
        DBManager.truncatePublicTables();
    }

    @Test
    public void testGenerate() throws Exception {
        dataInserter.generatePoints();
        dataInserter.generateRoutes();
        dataInserter.generateClients();
        dataInserter.generateUsers();
        dataInserter.generateRouteLists();
        dataInserter.generateRequests();
        // TODO call refreshMatView
    }
}
