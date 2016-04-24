package ru.logist.sbat.watchService;


import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.db.InsertOrUpdateResult;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.jsonParser.JsonPException;
import ru.logist.sbat.jsonParser.ValidatorException;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileChangeListener implements OnFileChangeListener {

    private static final Logger logger = LogManager.getLogger();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;
    public static final String RESPONSE_FILE_EXTENSION = ".ans";
    private static final String TEMP_FILE_EXTENSION = ".tmp";

    private final DBManager DBManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Path responceDir;
    private final Path backupDir;

    public FileChangeListener(DBManager DBManager, Path responceDir, Path backupDir) {
        this.DBManager = DBManager;
        this.responceDir = responceDir;
        this.backupDir = backupDir;
    }

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
                InsertOrUpdateResult insertOrUpdateResult = DBManager.updateDataFromJSONObject(dataFrom1c);
                logger.info("Update data finished, status = [{}]", insertOrUpdateResult);
                writeDbWorkResponse(insertOrUpdateResult);
            } else {
                writeBadFileResponse(filePath);
            }

            removeIncomingFile(filePath);
        });
    }

    public void close() {
        executorService.shutdown();
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
        Path responsePath = responceDir.resolve(filePath.getFileName() + RESPONSE_FILE_EXTENSION);
        try {
            FileUtils.writeStringToFile(responsePath.toFile(), "Data format error . The details in the log file.", StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Can't write response into [{}], exit application", responsePath);
            logger.error(e);
            System.exit(-1);
        }
    }

    private void writeDbWorkResponse(InsertOrUpdateResult insertOrUpdateResult) {
        logger.info("Start write result data into response directory");
        Path responsePath = responceDir.resolve(insertOrUpdateResult.getServer() + RESPONSE_FILE_EXTENSION);
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
        String backupFileName = currentDateString + filePath.getFileName();
        Path backupFilePath = backupDir.resolve(backupFileName);

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