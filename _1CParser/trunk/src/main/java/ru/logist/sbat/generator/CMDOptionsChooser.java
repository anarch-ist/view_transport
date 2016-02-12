package ru.logist.sbat.generator;

import ru.logist.sbat.db.DataBase;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.watchService.OnFileChangeListener;
import ru.logist.sbat.watchService.WatchServiceStarter;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class CMDOptionsChooser {
    // create cmd parser and options
    Scanner scanner = new Scanner(System.in);
//    while (true) {
//        System.out.println("choose mode: \n1 - parse\n2 - generate");
//        String nextLine = scanner.nextLine();
//        try {
//            if (Integer.parseInt(nextLine) == 1) {
//                System.out.println("1");
//                // start watch service here
//                WatchServiceStarter watchServiceStarter = new WatchServiceStarter(exchangeDir);
//                final DataBase finalDataBase = dataBase;
//                System.out.println("2");
//                watchServiceStarter.setOnFileChanged(new OnFileChangeListener() {
//                    @Override
//                    public void onFileCreate(Path filePath) {
//                        try {
//                            logger.info("start to updateDataFromFile [{}]", filePath);
//                            finalDataBase.updateDataFromJSONObject(JSONReadFromFile.read(filePath));
//                        } catch (SQLException | IOException | org.json.simple.parser.ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                try {
//                    watchServiceStarter.doWatch();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("3");
//
//                break;
//            } else if (Integer.parseInt(nextLine) == 2) {
//                System.out.println(2);
//                break;
//            }
//        } catch (NumberFormatException ignored) {/*NOPE*/}
//    }
}

//
//    public void startGeneration() {
//        timer = new Timer("insert timer", true);
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    if (isGenerateInsertIntoRequestTable)
//                        dataBase.generateInsertIntoRequestTable();
//                    if (isGenerateInsertIntoRouteListsTable)
//                        dataBase.generateInsertIntoRouteListsTable();
//                    if (isGenerateInsertIntoInvoicesTable)
//                        dataBase.generateInsertIntoInvoicesTable();
//                    if (isGenerateUpdateInvoiceStatuses)
//                        dataBase.generateUpdateInvoiceStatuses();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    close();
//                    timer.cancel();
//                    logger.info("connection closed");
//                    System.exit(-1);
//                }
//            }
//        };
//        timer.schedule(timerTask, 1_000, generatePeriod);
//    }
