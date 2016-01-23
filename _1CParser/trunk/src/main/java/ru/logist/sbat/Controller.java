package ru.logist.sbat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private static final Logger logger = LogManager.getLogger(Controller.class);
    private final Integer generatePeriod;
    private DataBase dataBase;
    private Timer timer;
    volatile private boolean isGenerateInsertIntoRequestTable = false;
    volatile private boolean isGenerateInsertIntoRouteListsTable = false;
    volatile private boolean isGenerateInsertIntoInvoicesTable = false;
    volatile private boolean isGenerateUpdateInvoiceStatuses = false;
    public Controller(Integer generatePeriod) {
        this.generatePeriod = generatePeriod;
    }

    public void setDataBase(DataBase dataBase) {
        this.dataBase = dataBase;
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
        timer.schedule(timerTask, 1_000, generatePeriod);
    }

    public void close() {
        dataBase.closeConnectionQuietly();
        if (timer != null)
            timer.cancel();
    }

}
