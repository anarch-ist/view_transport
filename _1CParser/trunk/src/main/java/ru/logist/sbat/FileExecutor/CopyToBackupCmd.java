package ru.logist.sbat.FileExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CopyToBackupCmd implements Command<Void>{
    private static final Logger logger = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;

    private Path backupDir;
    private Path filePath;

    public void setBackupDir(Path backupDir) {
        this.backupDir = backupDir;
    }
    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Void execute() throws CommandException {
        copyToBackup(filePath);
        return null;
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


}
