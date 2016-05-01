package ru.logist.sbat.resourcesInit;

import org.apache.logging.log4j.Logger;
import ru.logist.sbat.jsonParser.jsonReader.Util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Eagerly creates all resources
 */
public class SystemResourcesContainer {
    private final PropertiesPojo propertiesPojo;
    private Connection connection;
    private Path jsonDataDir;
    private Path backupDir;
    private Path responseDir;
    private Path logsDir;

    public SystemResourcesContainer(PropertiesPojo propertiesPojo, Logger logger) throws ResourceInitException {
        this.propertiesPojo = propertiesPojo;
        logger.info("try to connect to database...");
        createConnection();
        logger.info("connection recieved");
        createPaths();
        logger.info("directory paths checked");
    }

    public Connection getConnection() {
        return connection;
    }

    public Path getJsonDataDir() {
        return jsonDataDir;
    }

    public Path getBackupDir() {
        return backupDir;
    }

    public Path getResponseDir() {
        return responseDir;
    }

    public Path getLogsDir() {
        return logsDir;
    }

    public void createConnection() throws ResourceInitException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    propertiesPojo.getUrl() + propertiesPojo.getDbName() + "?" + propertiesPojo.getEncoding() + "&" + "noAccessToProcedureBodies=true",
                    propertiesPojo.getUser(),
                    propertiesPojo.getPassword()
            );
            connection.setAutoCommit(false);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new ResourceInitException("can't get connection to database", e);
        }
    }

    public void createPaths() throws ResourceInitException {
        this.jsonDataDir = createPathWithCheck(propertiesPojo.getJsonDataDir());
        this.backupDir = createPathWithCheck(propertiesPojo.getBackupDir());
        this.responseDir = createPathWithCheck(propertiesPojo.getResponseDir());
        this.logsDir = createPathWithCheck(propertiesPojo.getLogsDir());
    }

    public static Path createPathWithCheck(String string) throws ResourceInitException {
        Path path = Paths.get(string);
        if (!path.toFile().exists())
            throw new ResourceInitException(Util.getParameterizedString("directory {} is not exist", path));
        return path;
    }

}
