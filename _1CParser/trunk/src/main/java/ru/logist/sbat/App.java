package ru.logist.sbat;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.cmd.CmdLineParser;
import ru.logist.sbat.cmd.Option;
import ru.logist.sbat.cmd.Options;
import ru.logist.sbat.db.DataBase;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.watchService.OnFileChangeListener;
import ru.logist.sbat.watchService.WatchServiceStarter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class App {
    private static Properties properties;
    static {
        // get All settings from file, logger initialization
        properties = getProperties();
    }
    private static final Logger logger = LogManager.getLogger();
    private static Thread watchServiceThread;
    private static Path jsonDataDir;
    private static DataBase dataBase;
    private static volatile WatchServiceStarter watchServiceStarter;


    public static void main(String[] args) {

        // create database connection
        createDatabaseConnection(properties);

        // create separate thread that observe JsonData directory for new files, and starts if new files appeared
        createWatchService();

        // create cmd parser and options
        Scanner scanner = new Scanner(System.in);
        Options options = new Options();
        Option exit = new Option("exit");
        Option help = new Option("help");
        options.addAll(Arrays.asList(exit, help));

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
            Option option = optionListPair.getKey();

            if (option.equals(exit)) {
                closeAllAndExit();
            } else if (option.equals(help)) {
                System.out.println(options);
            }
        }
    }

    public static void closeAllAndExit() {
        dataBase.close();
        watchServiceThread.interrupt();
        watchServiceStarter.closeWatchService();
        System.exit(0);
    }

    private static void createWatchService() {
        // create watchService
        watchServiceStarter = new WatchServiceStarter(jsonDataDir);

        watchServiceStarter.setOnFileChanged(new OnFileChangeListener() {
            @Override
            public void onFileCreate(Path filePath) {
                try {
                    logger.info("start update data from file [{}]", filePath);
                    dataBase.updateDataFromJSONObject(JSONReadFromFile.read(filePath));
                } catch (SQLException | IOException | org.json.simple.parser.ParseException e) {
                    logger.error(e);
                    closeAllAndExit();
                }
            }
        });

        // create thread for observing exchange directory
        watchServiceThread = new Thread(() -> {
            try {
                watchServiceStarter.doWatch();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
            logger.info("database connection succefully recieved, URL: [" + url + dbName + "] " + "User: [" + user + "]");
        } catch (SQLException e) {
            logger.error(e);
            System.exit(-1);
        }
    }

    private static Properties getProperties() {

        File configFile = null;
        Path configPath = Paths.get(System.getProperty("user.home")).resolve("parser").resolve("config.property");
        configFile = configPath.toFile();
        if (!configFile.exists()) {
            System.out.println("config file not exist, exit application");
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


