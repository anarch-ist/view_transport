package ru.logist.sbat.fileExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CopyToBackupCmd {
    private static final Logger logger = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;

    private Path backupDir;
    private Path filePath;

    public CopyToBackupCmd(Path backupDir, Path filePath) {
        this.backupDir = backupDir;
        this.filePath = filePath;
    }

    public void execute() throws IOException {
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(backupDir);
        copyToBackup(filePath);
    }

    private void copyToBackup(Path filePath) throws IOException {
        String currentDateString = "_" + dateFormat.format(new Date()) + "_";
        String backupFileName = currentDateString + filePath.getFileName();
        Path backupFilePath = backupDir.resolve(backupFileName);

        logger.info("start backup file: [{}]", filePath);
        FileUtils.copyFile(filePath.toFile(), backupFilePath.toFile());
        logger.info("file was backuped: [{}]", backupFilePath);
    }
}
