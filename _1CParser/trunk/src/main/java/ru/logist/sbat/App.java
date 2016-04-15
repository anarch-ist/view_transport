package ru.logist.sbat;
import com.djdch.log4j.StaticShutdownCallbackRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import ru.logist.sbat.cmd.CmdLineParser;
import ru.logist.sbat.cmd.Option;
import ru.logist.sbat.cmd.Options;
import ru.logist.sbat.cmd.Pair;
import ru.logist.sbat.db.DataBase;
import ru.logist.sbat.db.InsertOrUpdateResult;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.jsonParser.JsonPException;
import ru.logist.sbat.jsonParser.ValidatorException;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;
import ru.logist.sbat.watchService.OnFileChangeListener;
import ru.logist.sbat.watchService.WatchServiceStarter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO refactor this class
public class App {
    private static final String RESPONSE_FILE_EXTENSION = ".ans";
    private static final String TEMP_FILE_EXTENSION = ".tmp";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;

    private static Properties properties;
    static {
        System.setProperty("log4j.shutdownCallbackRegistry", "com.djdch.log4j.StaticShutdownCallbackRegistry");
        // get All settings from file, logger initialization
        properties = getProperties();
    }
    private static final Logger logger = LogManager.getLogger();
    private static Thread watchServiceThread;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static Path jsonDataDir;
    private static DataBase dataBase;
    private static volatile WatchServiceStarter watchServiceStarter;


    public static void main(String[] args) {

        // create database connection
        createDatabaseConnection(properties);

        // create separate thread that observe JsonData directory for new files, and starts if new files appeared
        createWatchService();

        // add shudownhook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                closeAll();
                logger.error("SHUTDOWN");
                StaticShutdownCallbackRegistry.invoke();
            }
        });

        // create cmd parser and options, blocking method
        createOptionsAndStartScanner();
    }

    private static void createOptionsAndStartScanner() {
        Scanner scanner = new Scanner(System.in);
        Options options = new Options();
        Option quit = new Option("quit");
        Option help = new Option("help");
        options.addAll(Arrays.asList(quit, help));

        CmdLineParser cmdLineParser = new CmdLineParser(options);
        while (true) {
            String nextLine = scanner.nextLine();
            Pair<Option, List<String>> optionListPair;
            try {
                optionListPair = cmdLineParser.parse(nextLine);
            } catch (ParseException e) {
                logger.debug(e.getMessage());
                continue;
            }
            Option option = optionListPair.getFirst();

            if (option.equals(quit)) {
                System.exit(0);
            } else if (option.equals(help)) {
                System.out.println(options);
            }
        }
    }

    private static synchronized void closeAll() {
        dataBase.close();
        logger.info("Database connection closed");
        watchServiceThread.interrupt();
        watchServiceStarter.closeWatchService();
        logger.info("Watch service closed.");
        executorService.shutdown();
        logger.info("Executor service closed");
    }

    private static void createWatchService() {

        watchServiceStarter = new WatchServiceStarter(jsonDataDir);

        watchServiceStarter.setOnFileChanged(new FileChangeListener());

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

    private static void createDatabaseConnection(Properties properties) {
        try {
            String url = properties.getProperty("url");
            String dbName = properties.getProperty("dbName");
            String user = properties.getProperty("user");
            dataBase = new DataBase(
                    url,
                    dbName,
                    user,
                    properties.getProperty("password"),
                    properties.getProperty("encoding")
            );
            logger.info("database connection successfully received, URL: [" + url + dbName + "] " + "User: [" + user + "]");
        } catch (SQLException e) {
            logger.error(e);
            System.exit(-1);
        }
    }

    private static Properties getProperties() {

        Path configPath = Paths.get(System.getProperty("user.home")).resolve("parser").resolve("config.property");
        File configFile = configPath.toFile();
        if (!configFile.exists()) {
            System.out.println("config file not exist at path = [" + configFile + "], exit application");
            System.exit(-1);
        }

        Properties properties = null;
        try {
            properties = new Properties();
            properties.loadFromXML(new FileInputStream(configFile));
            System.out.println("DATABASE CONNECTION PARAMS:");
            System.out.println("url = [" + properties.getProperty("url") + "]");
            System.out.println("user = ["+properties.getProperty("user") + "]");
            System.out.println("password = [" + properties.getProperty("password") + "]");
            System.out.println("dbName = [" + properties.getProperty("dbName") + "]");
            System.out.println("encoding = [" + properties.getProperty("encoding") + "]");
            System.out.println("DIRECTORY PARAMS:");

            System.out.println("data directory = [" + properties.getProperty("jsonDataDir") + "]");
            jsonDataDir = Paths.get(properties.getProperty("jsonDataDir"));
            if (!jsonDataDir.toFile().exists()) throw new IllegalArgumentException("directory is not exist");

            System.out.println("backup directory = [" + properties.getProperty("backupDir") + "]");
            Path backupDir = Paths.get(properties.getProperty("backupDir"));
            if (!backupDir.toFile().exists()) throw new IllegalArgumentException("directory is not exist");

            System.out.println("response directory = [" + properties.getProperty("responseDir") + "]");
            Path responseDir = Paths.get(properties.getProperty("responseDir"));
            if (!responseDir.toFile().exists()) throw new IllegalArgumentException("directory is not exist");

            System.out.println("logs directory = [" + properties.getProperty("logsDir") + "]");
            Path logsDir = Paths.get(properties.getProperty("logsDir"));
            if (!logsDir.toFile().exists()) throw new IllegalArgumentException("directory is not exist");

            System.setProperty("logFilename", logsDir.resolve("parser.log").toString());

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return properties;
    }

    private static class FileChangeListener implements OnFileChangeListener {
        @Override
        public void onFileCreate(Path filePath) {

            // start executor when downloading finishes
            if (filePath.toString().endsWith(TEMP_FILE_EXTENSION))
                return;

            executorService.submit((Runnable) () -> {

                waitForFileReleaseLock(filePath);

                DataFrom1c dataFrom1c = null;
                try {
                    logger.info("Start creating dataFrom1c object from file [{}]", filePath);
                    dataFrom1c = JSONReadFromFile.getJsonObjectFromFile(filePath);
                    logger.info("dataFrom1c object succefully recieved ");
                } catch (IOException e) {
                    logger.error("Can't read file [{}]", filePath);
                    logger.error(e);
                } catch (org.json.simple.parser.ParseException | JsonPException e) {
                    logger.error("Illegal JSON format [{}]", filePath);
                    logger.error(e);
                } catch (ValidatorException e) {
                    logger.error("Illegal JSON constraint format [{}]", filePath);
                    logger.error(e);
                }

                copyToBackup(filePath);

                if (dataFrom1c != null) {
                    InsertOrUpdateResult insertOrUpdateResult = dataBase.updateDataFromJSONObject(dataFrom1c);
                    logger.info("Update data finished, status = [{}]", insertOrUpdateResult);
                    writeDbWorkResponse(insertOrUpdateResult);
                } else {
                    writeBadFileResponse(filePath);
                }

                removeIncomingFile(filePath);
            });
        }

        private void waitForFileReleaseLock(Path filePath) {
            for (int i = 0; i < 10; i++) {
                BufferedReader bufferedReader = null;
                try {
                    bufferedReader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
                    bufferedReader.close();
                    break;
                } catch (FileSystemException e) {
                    logger.warn("file [{}] locked, wait for release...", filePath);
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e1) {/*NOPE*/}

                } catch (IOException e) {
                    /*NOPE*/
                } finally {
                    try {
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (IOException e) {/*NOPE*/}
                }
            }
        }

        private void writeBadFileResponse(Path filePath) {
            String responseDir = properties.getProperty("responseDir");
            Path responsePath = Paths.get(responseDir).resolve(filePath.getFileName() + RESPONSE_FILE_EXTENSION);
            try {
                FileUtils.writeStringToFile(responsePath.toFile(), "Data format error . The details in the log file.", StandardCharsets.UTF_8);
            } catch (IOException e) {
                logger.error("Can't write response into [{}], exit application", responsePath);
                logger.error(e);
                System.exit(-1);
            }
        }

        private void writeDbWorkResponse(InsertOrUpdateResult insertOrUpdateResult) {
            String responseDir = properties.getProperty("responseDir");
            logger.info("Start write result data into response directory");
            Path responsePath = Paths.get(responseDir).resolve(insertOrUpdateResult.getServer() + RESPONSE_FILE_EXTENSION);
            String resultString = insertOrUpdateResult.toString();
            try {
                FileUtils.writeStringToFile(responsePath.toFile(), resultString, StandardCharsets.UTF_8);
                logger.info("Response data were successfully written");
            } catch (IOException e) {
                logger.error("Can't write response into [{}], exit application", responsePath);
                logger.error(e);
                System.exit(-1);
            }
        }

        private void copyToBackup(Path filePath) {
            String currentDateString = "_" + dateFormat.format(new Date()) + "_";
            String backupDir = properties.getProperty("backupDir");
            String backupFileName = currentDateString + filePath.getFileName();
            Path backupFilePath = Paths.get(backupDir).resolve(backupFileName);

            logger.info("start backup file: [{}]", filePath);
            try {
                FileUtils.copyFile(filePath.toFile(), backupFilePath.toFile());
                logger.info("file was backuped: [{}]", backupFilePath);
            } catch (IOException e) {
                logger.error("Can't copy [{}] to [{}], exit application", filePath, backupFilePath);
                logger.error(e);
                System.exit(-1);
            }
        }

        private void removeIncomingFile(Path filePath) {
            logger.info("start delete file: [{}]", filePath);
            try {
                FileUtils.forceDelete(filePath.toFile());
                logger.info("file was deleted: [{}]", filePath);
            } catch (IOException e) {
                logger.error("Can't delete [{}], exit application", filePath);
                logger.error(e);
                System.exit(-1);
            }
        }
    }
}