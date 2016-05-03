package ru.logist.sbat.watchService;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.fileExecutor.*;
import ru.logist.sbat.db.DBManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileChangeListener implements OnFileChangeListener {

    private static final Logger logger = LogManager.getLogger();


    private static final String TEMP_FILE_EXTENSION = ".tmp";

    private final DBManager dbManager;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Path responseDir;
    private final Path backupDir;

    public FileChangeListener(DBManager dbManager, Path responseDir, Path backupDir) {
        this.dbManager = dbManager;
        this.responseDir = responseDir;
        this.backupDir = backupDir;
    }

    @Override
    public void onFileCreate(Path filePath) {

        // start executor when downloading finishes
        if (filePath.toString().endsWith(TEMP_FILE_EXTENSION))
            return;

        executorService.submit((Runnable) () -> {

            waitForFileReleaseLock(filePath);

            try {
                new CommandsBuilder()
                        .setResources(dbManager, filePath, responseDir, backupDir)
                        .executeAll();
            } catch (CommandException e) {
                logger.error(e);
            }
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




}