package ru.logist.sbat;
import com.djdch.log4j.StaticShutdownCallbackRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.cmd.CmdLineParser;
import ru.logist.sbat.cmd.Option;
import ru.logist.sbat.cmd.Options;
import ru.logist.sbat.cmd.Pair;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.db.TransactionResult;
import ru.logist.sbat.jsonParser.jsonReader.JsonPException;
import ru.logist.sbat.jsonParser.jsonReader.ValidatorException;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;
import ru.logist.sbat.resourcesInit.ResourceInitException;
import ru.logist.sbat.resourcesInit.SystemResourcesContainer;
import ru.logist.sbat.watchService.FileChangeListener;
import ru.logist.sbat.resourcesInit.PropertiesPojo;
import ru.logist.sbat.watchService.WatchServiceStarter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

public class App {

    private static SystemResourcesContainer systemResourcesContainer;
    private static Logger logger = null;
    // read property file and init logger
    static {
        PropertiesPojo propertiesPojo = null;
        try {
            propertiesPojo = getProperties();
            logger = initLogger(propertiesPojo.getLogsDir());
        } catch (ResourceInitException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        try {
            systemResourcesContainer = new SystemResourcesContainer(propertiesPojo, logger);
        } catch (ResourceInitException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

    }
    private static Thread watchServiceThread;
    private static DBManager dbManager;
    private static volatile WatchServiceStarter watchServiceStarter;
    private static FileChangeListener onFileChangeListener;


    public static void main(String[] args) {

        // create database helper object
        dbManager = new DBManager(systemResourcesContainer.getConnection());

        // create separate thread that observe JsonData directory for new files, and starts handler if new files appeared
        createWatchService();

        // add shudownhook that closes all resources
        createShutdownHook();

        // go through json data directory and process all existing files
        processExistingFiles();

        // create cmd parser and options, blocking method
        createOptionsAndStartScanner();
    }

    private static void processExistingFiles() {
        Path jsonDataDir = systemResourcesContainer.getJsonDataDir();
        try {
            Files.newDirectoryStream(jsonDataDir).forEach(path -> onFileChangeListener.onFileCreate(path));
        } catch (IOException e) {
            logger.error(GlobalUtils.getParameterizedString("can't read directory {}", jsonDataDir));
            System.exit(-1);
        }
    }

    private static void createShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                closeAll();
                logger.info("SHUTDOWN");
                StaticShutdownCallbackRegistry.invoke();
            }
        });
    }

    private static void createOptionsAndStartScanner() {
        Scanner scanner = new Scanner(System.in);
        Options options = new Options();
        Option quit = new Option("quit");
        Option help = new Option("help");
        Option rollback = new Option("rollback", "-date");

        options.addAll(Arrays.asList(quit, help, rollback));

        CmdLineParser cmdLineParser = new CmdLineParser(options);
        while (true) {
            String nextLine = scanner.nextLine();
            Pair<Option, Map<String, String>> optionListPair;
            try {
                optionListPair = cmdLineParser.parse(nextLine);
            } catch (ParseException e) {
                logger.error(e.getMessage());
                continue;
            }
            Option option = optionListPair.getFirst();

            if (option.equals(quit)) {
                System.exit(0);
            } else if (option.equals(help)) {
                System.out.println(options);
            } else if (option.equals(rollback)) {
                try {
                    Map<String, String> parameters = optionListPair.getSecond();
                    if (parameters.isEmpty()) {
                        rollback(new Timestamp(new java.util.Date().getTime()));
                    } else {
                        String timestampAsString = parameters.get(option.getParameters().get(0));
                        Timestamp timestamp = Timestamp.valueOf(timestampAsString);
                        rollback(timestamp);
                    }
                } catch (Exception e) {
                    logger.error(e);
                    System.exit(-1);
                }
            }
        }
    }

    private static void rollback(Timestamp timestamp) throws IOException, org.json.simple.parser.ParseException, JsonPException, ValidatorException {
        logger.info("start copy exchange table");
        dbManager.createTempTable();
        logger.info("start select all data from exchange table");
        List<String> dataStrings = dbManager.selectAllFromExchange(timestamp);
        logger.info("start clear rows in exchange table before [{}]", timestamp);
        dbManager.removeFromExchange(timestamp);
        logger.info("start clear main database");
        dbManager.truncatePublicTables();
        Path responseDir = systemResourcesContainer.getResponseDir();
        FileUtils.cleanDirectory(responseDir.toFile());
        logger.info("response directory cleaned");

        for (String dataString : dataStrings) {
            DataFrom1c dataFrom1c = new DataFrom1c(JSONReadFromFile.getJsonObjectFromString(dataString));
            TransactionResult transactionResult = dbManager.updateDataFromJSONObject(dataFrom1c);
            Path responsePath = responseDir.resolve(transactionResult.getServer() + FileChangeListener.RESPONSE_FILE_EXTENSION);
            FileUtils.writeStringToFile(responsePath.toFile(), transactionResult.toString(), StandardCharsets.UTF_8);
        }
        // remove data from exchange before timestamp
        dbManager.removeTempTable();
        logger.info("temp exchange table removed");
        logger.info("ALL DATA ROLLBACKED SUCCESSFULLY timestamp = [{}]", timestamp);
    }



    private static synchronized void closeAll() {
        if (dbManager != null) {
            dbManager.close();
            logger.info("Database connection closed.");
        }
        if (watchServiceThread != null) {
            watchServiceThread.interrupt();
            logger.info("Watch service thread interrupted.");
        }
        if (watchServiceStarter != null) {
            watchServiceStarter.closeWatchService();
            logger.info("Watch service starter closed.");
        }
        if (onFileChangeListener != null) {
            onFileChangeListener.close();
            logger.info("Executor service closed.");
        }
    }

    private static void createWatchService() {

        watchServiceStarter = new WatchServiceStarter(systemResourcesContainer.getJsonDataDir());

        onFileChangeListener = new FileChangeListener(
                dbManager,
                systemResourcesContainer.getResponseDir(),
                systemResourcesContainer.getBackupDir());

        watchServiceStarter.setOnFileChanged(onFileChangeListener);

        // create thread for observing exchange directory
        watchServiceThread = new Thread(() -> {
            try {
                watchServiceStarter.doWatch();
            } catch (IOException e) {
                logger.error("Watch service error");
                logger.error(e);
            }
        });
        watchServiceThread.setName("watchServiceThread");
        watchServiceThread.setDaemon(true);
        watchServiceThread.start();
    }

    private static PropertiesPojo getProperties() throws ResourceInitException {
        Path configPath = Paths.get(System.getProperty("user.home")).resolve("parser").resolve("config.property");
        File configFile = configPath.toFile();
        if (!configFile.exists()) {
            throw new ResourceInitException(GlobalUtils.getParameterizedString("config file not exist at path = {}, exit application", configPath));
        }
        try {
            Properties properties = new Properties();
            properties.loadFromXML(new FileInputStream(configFile));
            PropertiesPojo propertiesPojo = new PropertiesPojo(properties);
            System.out.println(propertiesPojo.toString());
            return propertiesPojo;
        } catch (IOException e) {
            throw new ResourceInitException(GlobalUtils.getParameterizedString("can't read file = {}, exit application", configFile));
        }
    }

    private static Logger initLogger(String logsDir) throws ResourceInitException {
        Path logsDirPath = SystemResourcesContainer.createPathWithCheck(logsDir);
        System.setProperty("log4j.shutdownCallbackRegistry", "com.djdch.log4j.StaticShutdownCallbackRegistry");
        System.setProperty("logFilename", logsDirPath.resolve("parser.log").toString());
        System.setProperty("errorLogFilename", logsDirPath.resolve("error.log").toString());
        return LogManager.getLogger();
    }


}