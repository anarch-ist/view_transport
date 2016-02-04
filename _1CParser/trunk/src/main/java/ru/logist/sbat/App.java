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
import ru.logist.sbat.properties.PropertiesManager;

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
    public static final String IRT = "IRT"; // insert into request table
    public static final String IRLT = "IRLT"; // insert into routeListTable
    public static final String IIT = "IIT"; // insert into invoices table
    public static final String UIT = "UIT"; // update invoice statuses

    public static String getLoggerFile( Logger log ) {
        org.apache.logging.log4j.core.Logger loggerImpl = (org.apache.logging.log4j.core.Logger) log;
        Appender appender = loggerImpl.getAppenders().get("FILE");
        return ((FileAppender) appender).getFileName();
    }

    public static void main(String[] args) {

        // show logs path
        Path rootDir = Paths.get(System.getProperty("user.dir"));


        logger.info("log file path: " + rootDir.resolve(getLoggerFile(logger)));

        // get All properties
        Properties properties = null;
        try {
            Path prefsPath = rootDir.resolve("prefs.property");
            PropertiesManager.setPrefsPath(prefsPath);
            properties = PropertiesManager.handleProperties();
            logger.info("path to preferences directory: " + PropertiesManager.getPrefsPath());
            logger.info("property file content: " + properties);
        } catch (IOException e) {
            logger.error(e);
            System.exit(-1);
        }

        // create database connection
        DataBase dataBase = null;
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

        // create controller
        Controller controller = new Controller(Integer.parseInt(properties.getProperty("generatePeriod")) * 1_000);
        controller.setDataBase(dataBase);

        // create cmd parser and options
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("choose mode: \n1 - parse\n2 - generate");
            String nextLine = scanner.nextLine();
            try {
                if (Integer.parseInt(nextLine) == 1) {
                    System.out.println(1);
                    break;
                } else if (Integer.parseInt(nextLine) == 2) {
                    System.out.println(2);
                    break;
                }
            } catch (NumberFormatException ignored) {/*NOPE*/}
        }

        Options options = new Options();
        Option exit = new Option("exit");
        Option start = new Option("start");
        Option help = new Option("help");
        Option setActions = new Option("setActions", IRT, IRLT, IIT, UIT);
        options.addAll(Arrays.asList(exit, start, setActions, help));

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
                controller.close();
                System.exit(0);
            } else if (option.equals(start)) {
                controller.startGeneration();
            } else if (option.equals(setActions)) {
                controller.setGenerateInsertIntoRequestTable(parameters.contains(IRT));
                controller.setGenerateInsertIntoRouteListsTable(parameters.contains(IRLT));
                controller.setGenerateInsertIntoInvoicesTable(parameters.contains(IIT));
                controller.setGenerateUpdateInvoiceStatuses(parameters.contains(UIT));
            } else if (option.equals(help)) {
                System.out.println(options);
            }

        }

    }
}


