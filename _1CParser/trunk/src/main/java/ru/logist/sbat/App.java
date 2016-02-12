package ru.logist.sbat;

import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.FileAppender;
import ru.logist.sbat.cmd.CmdLineParser;
import ru.logist.sbat.cmd.Option;
import ru.logist.sbat.cmd.Options;
import ru.logist.sbat.db.DataBase;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.properties.PropertiesManager;
import ru.logist.sbat.watchService.OnFileChangeListener;
import ru.logist.sbat.watchService.WatchServiceStarter;

import java.io.File;
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
    private static final Logger logger = LogManager.getLogger();
    public static final String EXCHANGE_DIR_NAME = "exchange/";
    private static Thread watchServiceThread;

    public static String getLoggerFile( Logger log ) {
        org.apache.logging.log4j.core.Logger loggerImpl = (org.apache.logging.log4j.core.Logger) log;
        Appender appender = loggerImpl.getAppenders().get("FILE");
        return ((FileAppender) appender).getFileName();
    }

    public static void closeAllAndExit() {
        dataBase.close();
        watchServiceThread.interrupt();
        watchServiceStarter.closeWatchService();
        System.exit(0);
    }

    private static DataBase dataBase;
    private static volatile WatchServiceStarter watchServiceStarter;

    public static void main(String[] args) {

        // show logs path and exchange path
        Path rootDir = Paths.get(System.getProperty("user.dir"));
        Path exchangeDir = rootDir.resolve(EXCHANGE_DIR_NAME);
        File file = exchangeDir.toFile();
        if (!file.exists()) {
            if (file.mkdir())
                logger.info("exchange directory created");
        }
        logger.info("path to exchange directory: [{}]", exchangeDir);
        logger.info("log file path: [{}]", rootDir.resolve(getLoggerFile(logger)));

        // get All properties
        Properties properties = null;
        try {
            Path prefsPath = rootDir.resolve("prefs.property");
            PropertiesManager.setPrefsPath(prefsPath);
            properties = PropertiesManager.handleProperties();
            logger.info("path to preferences directory:[{}] ", PropertiesManager.getPrefsPath());
            logger.info("property file content: [{}]", properties);
        } catch (IOException e) {
            logger.error(e);
            System.exit(-1);
        }

        // create database connection
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

        // create watchService
        watchServiceStarter = new WatchServiceStarter(exchangeDir);

        watchServiceStarter.setOnFileChanged(new OnFileChangeListener() {
            @Override
            public void onFileCreate(Path filePath) {
                try {
                    logger.info("start to updateDataFromFile [{}]", filePath);
                    //dataBase.truncatePublicTables();
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
        watchServiceThread.start();

//        // add shutdown hook
//        Runtime.getRuntime().addShutdownHook(new Thread()
//        {
//            @Override
//            public void run() {
//                closeAllAndExit();
//                System.out.println("close and EXIT");
//            }
//        });

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
            List<String> parameters = optionListPair.getValue();

            if (option.equals(exit)) {
                closeAllAndExit();
            } else if (option.equals(help)) {
                System.out.println(options);
            }

        }

    }
}


