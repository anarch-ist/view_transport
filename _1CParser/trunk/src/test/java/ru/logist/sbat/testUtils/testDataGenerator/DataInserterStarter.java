package ru.logist.sbat.testUtils.testDataGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.resourcesInit.ResourceInitException;
import ru.logist.sbat.resourcesInit.SystemResourcesContainer;
import ru.logist.sbat.resourcesInit.PropertiesPojo;
import ru.logist.sbat.testUtils.TestHelper;

import java.io.IOException;
import java.util.Properties;

public class DataInserterStarter {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws ResourceInitException, IOException {
        // get connection to database
        Properties testProperties = new Properties();
        testProperties.loadFromXML(TestHelper.class.getResourceAsStream("test_config.property"));
        SystemResourcesContainer systemResourcesContainer = new SystemResourcesContainer(new PropertiesPojo(testProperties), logger);
        DBManager DBManager = new DBManager(systemResourcesContainer.getConnection());
        DataInserter dataInserter = new DataInserter(systemResourcesContainer.getConnection());
        DBManager.truncatePublicTables();

        dataInserter.generatePoints();
        dataInserter.generateRoutes();
        dataInserter.generateClients();
        dataInserter.generateUsers();
        dataInserter.generateRouteLists();
        dataInserter.generateRequests();
    }

}
