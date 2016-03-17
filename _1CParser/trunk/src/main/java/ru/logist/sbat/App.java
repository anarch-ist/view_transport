package ru.logist.sbat;
import ru.logist.sbat.cmd.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import ru.logist.sbat.cmd.CmdLineParser;
import ru.logist.sbat.cmd.Option;
import ru.logist.sbat.cmd.Options;
import ru.logist.sbat.db.DataBase;
import ru.logist.sbat.db.InsertOrUpdateResult;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;
import ru.logist.sbat.watchService.OnFileChangeListener;
import ru.logist.sbat.watchService.WatchServiceStarter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    private static Properties properties;
    static {
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
                closeAllAndExit();
            } else if (option.equals(help)) {
                System.out.println(options);
            }
        }
    }

    public static void closeAllAndExit() {
        dataBase.close();
        logger.info("Database connection closed");
        watchServiceThread.interrupt();
        watchServiceStarter.closeWatchService();
        logger.info("Watch service closed.");
        executorService.shutdown();
        logger.info("Executor service closed");
        System.exit(0);
    }

    private static void createWatchService() {

        watchServiceStarter = new WatchServiceStarter(jsonDataDir);

        watchServiceStarter.setOnFileChanged(new OnFileChangeListener() {
            @Override
            public void onFileCreate(Path filePath) {

                executorService.submit((Runnable) () -> {

                    File file = filePath.toFile();

                    // wait for loading all data
                    long currentFileSize = -1;
                    while (currentFileSize != file.length()) {
                        currentFileSize = file.length();
                        try {
                            Thread.currentThread().sleep(1000);
                        } catch (InterruptedException e) {/*NOPE*/}
                        logger.info("wait for loading... file size = [{}], size second ago = [{}]", file.length(), currentFileSize);
                    }

                    try {
                        logger.info("Start update data from file [{}]", filePath);
                        DataFrom1c dataFrom1c = JSONReadFromFile.read(filePath);
                        InsertOrUpdateResult insertOrUpdateResult = dataBase.updateDataFromJSONObject(dataFrom1c);
                        logger.info("Update data finished, status = [{}]", insertOrUpdateResult);
                        logger.info("Start write result data into response directory");
                        String responseDir = properties.getProperty("responseDir");
                        Path responsePath = Paths.get(responseDir).resolve("response.txt");
                        String resultString = insertOrUpdateResult.toString();
                        try {
                            FileUtils.writeStringToFile(responsePath.toFile(), resultString, StandardCharsets.UTF_8);
                            logger.info("Response data were successfully written");
                        } catch (IOException e) {
                            logger.error("Can't write response into [{}]", responsePath);
                            logger.error(e);
                        }

                        logger.info("Delete file: [{}]", filePath);
                        try {
                            FileUtils.forceDelete(filePath.toFile());
                            logger.info("file was deleted: [{}]", filePath);
                        } catch (IOException e) {
                            logger.error("Can't delete [{}]", filePath);
                            logger.error(e);
                        }

                    } catch (IOException e) {
                        logger.error("Can't read file [{}]", filePath);
                        logger.error(e);
                    } catch (org.json.simple.parser.ParseException e) {
                        logger.error("Illegal JSON file format [{}]", filePath);
                        logger.error(e);
                    }
                });

            }
        });

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
}


