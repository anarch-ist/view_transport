package ru.logist.sbat;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private Properties properties;
    private final DataBase dataBase;
    private Timer timer;


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
        String pattern = "#######.##";
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat)nf;
        df.applyPattern(pattern);
        TimerTask insertIntoTariffs = new TimerTask() {
            @Override
            public void run() {
                try {
                    dataBase.generateInsertIntoRequestTable();
                    dataBase.generateInsertIntoRouteListsTable();
                    dataBase.generateInsertIntoInvoicesTable();
                    System.out.println(dataBase.getInvoiceStatuses());
                } catch (SQLException e) {
                    e.printStackTrace();
                    close();
                    timer.cancel();
                    System.out.println("connection closed");
                    System.exit(-1);
                }
            }
        };
        timer.schedule(insertIntoTariffs, 1_000, 5_000);
    }

    public void close() {
        try {
            dataBase.closeConnection();
            if (timer != null)
                timer.cancel();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
