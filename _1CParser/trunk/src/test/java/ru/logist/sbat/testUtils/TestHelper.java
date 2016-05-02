package ru.logist.sbat.testUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.fileExecutor.CommandException;
import ru.logist.sbat.fileExecutor.FileToStringCmd;
import ru.logist.sbat.fileExecutor.JsonToBeanCmd;
import ru.logist.sbat.fileExecutor.StringToJsonCmd;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.jsonToBean.beans.DataFrom1c;
import ru.logist.sbat.resourcesInit.PropertiesPojo;
import ru.logist.sbat.resourcesInit.ResourceInitException;
import ru.logist.sbat.resourcesInit.SystemResourcesContainer;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

public class TestHelper {
    private static final Logger logger = LogManager.getLogger();
    private DBManager dbManager;

    public Connection prepareDatabase() throws IOException, ResourceInitException {
        // get connection to database
        Properties testProperties = new Properties();
        testProperties.loadFromXML(TestHelper.class.getResourceAsStream("test_config.property"));
        SystemResourcesContainer systemResourcesContainer = new SystemResourcesContainer(new PropertiesPojo(testProperties), logger);
        Connection connection = systemResourcesContainer.getConnection();
        dbManager = new DBManager(connection);
        // clean database content
        dbManager.truncatePublicTables();
        dbManager.removeFromExchange(new Timestamp(new java.util.Date().getTime()));
        return connection;
    }

    public void loadFile(Path path) throws CommandException, DBCohesionException, SQLException {
        FileToStringCmd fileToStringCmd = new FileToStringCmd();
        fileToStringCmd.setFilePath(path);
        StringToJsonCmd stringToJsonCmd = new StringToJsonCmd();
        stringToJsonCmd.setFileAsString(fileToStringCmd.execute());
        JsonToBeanCmd jsonToBeanCmd = new JsonToBeanCmd();
        jsonToBeanCmd.setJsonObject(stringToJsonCmd.execute());
        DataFrom1c dataFrom1c = jsonToBeanCmd.execute();
        dbManager.updateDataFromJSONObject(dataFrom1c);
    }

}
