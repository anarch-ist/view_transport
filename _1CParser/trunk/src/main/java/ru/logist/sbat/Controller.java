package ru.logist.sbat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class);
    private final DataBase dataBase;
    private Properties properties;
    private Timer timer;
    volatile private boolean isGenerateInsertIntoRequestTable = false;
    volatile private boolean isGenerateInsertIntoRouteListsTable = false;
    volatile private boolean isGenerateInsertIntoInvoicesTable = false;
    volatile private boolean isGenerateUpdateInvoiceStatuses = false;

    public Controller() {

        // get All properties
        try {
            properties = handleProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create database connection
        dataBase = new DataBase(
                properties.getProperty("url"),
                properties.getProperty("dbName"),
                properties.getProperty("user"),
                properties.getProperty("password")
        );


    }

    public void setGenerateInsertIntoRequestTable(boolean generateInsertIntoRequestTable) {
        isGenerateInsertIntoRequestTable = generateInsertIntoRequestTable;
    }

    public void setGenerateInsertIntoRouteListsTable(boolean generateInsertIntoRouteListsTable) {
        isGenerateInsertIntoRouteListsTable = generateInsertIntoRouteListsTable;
    }

    public void setGenerateInsertIntoInvoicesTable(boolean generateInsertIntoInvoicesTable) {
        isGenerateInsertIntoInvoicesTable = generateInsertIntoInvoicesTable;
    }

    public void setGenerateUpdateInvoiceStatuses(boolean generateUpdateInvoiceStatuses) {
        isGenerateUpdateInvoiceStatuses = generateUpdateInvoiceStatuses;
    }

    private Properties handleProperties() throws IOException {
        Path userDir = Paths.get(System.getProperty("user.dir"));
        Path prefsPath = userDir.resolve("prefs.property");
        Properties properties = new Properties();
        if (!prefsPath.toFile().exists()) {
            DefaultProperties defaultProperties = new DefaultProperties();
            try (FileOutputStream fos = new FileOutputStream(prefsPath.toFile())) {
                defaultProperties.storeToXML(fos, "prefs", "UTF-8");
                return defaultProperties;
            }
        } else {
            properties.loadFromXML(new FileInputStream(prefsPath.toFile()));
            return properties;
        }
    }


    public void startGeneration() {
        timer = new Timer("insert timer", true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (isGenerateInsertIntoRequestTable)
                        dataBase.generateInsertIntoRequestTable();
                    if (isGenerateInsertIntoRouteListsTable)
                        dataBase.generateInsertIntoRouteListsTable();
                    if (isGenerateInsertIntoInvoicesTable)
                        dataBase.generateInsertIntoInvoicesTable();
                    if (isGenerateUpdateInvoiceStatuses)
                        dataBase.generateUpdateInvoiceStatuses();
                } catch (SQLException e) {
                    e.printStackTrace();
                    close();
                    timer.cancel();
                    logger.info("connection closed");
                    System.exit(-1);
                }
            }
        };
        timer.schedule(timerTask, 1_000, Integer.parseInt(properties.getProperty("generatePeriod")) * 1_000);
    }

    public void close() {
        dataBase.closeConnectionQuietly();
        if (timer != null)
            timer.cancel();
    }
}
