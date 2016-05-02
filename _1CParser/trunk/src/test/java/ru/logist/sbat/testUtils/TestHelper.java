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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

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

    public DataFrom1c getBeanObjectFromFile(Path path) throws CommandException {
        FileToStringCmd fileToStringCmd = new FileToStringCmd();
        fileToStringCmd.setFilePath(path);

        StringToJsonCmd stringToJsonCmd = new StringToJsonCmd();
        String fileAsString = fileToStringCmd.execute();
        stringToJsonCmd.setFileAsString(fileAsString);

        JsonToBeanCmd jsonToBeanCmd = new JsonToBeanCmd();
        jsonToBeanCmd.setJsonObject(stringToJsonCmd.execute());
        return jsonToBeanCmd.execute();
    }

    public void loadFileInDatabase(Path path) throws CommandException, DBCohesionException, SQLException {
        dbManager.updateDataFromJSONObject(getBeanObjectFromFile(path));
    }

    public List<Path> listBackupFilesInOrder() throws URISyntaxException {
        List<Path> result = new ArrayList<>();
        Path backupDir = Paths.get(this.getClass().getResource("backup").toURI());
        String[] list = backupDir.toFile().list();
        Arrays.sort(list);
        for (String fileName : list) {
            result.add(backupDir.resolve(fileName));
        }
        return result;
    }

}
