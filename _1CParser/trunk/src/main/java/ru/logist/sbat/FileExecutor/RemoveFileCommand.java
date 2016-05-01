package ru.logist.sbat.FileExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class RemoveFileCommand implements Command<Void>{

    private static final Logger logger = LogManager.getLogger();
    private Path filePath;

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Void execute() throws CommandException {
        removeIncomingFile(filePath);
        return null;
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
