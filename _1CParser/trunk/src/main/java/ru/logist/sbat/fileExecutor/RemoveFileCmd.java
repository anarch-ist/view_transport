package ru.logist.sbat.fileExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class RemoveFileCmd {

    private static final Logger logger = LogManager.getLogger();
    private Path filePath;

    public RemoveFileCmd(Path filePath) {
        this.filePath = filePath;
    }

    public void execute() throws IOException {
        Objects.requireNonNull(filePath);
        removeIncomingFile(filePath);
    }

    private void removeIncomingFile(Path filePath) throws IOException {
        logger.info("start delete file: [{}]", filePath);
        FileUtils.forceDelete(filePath.toFile());
        logger.info("file was deleted: [{}]", filePath);
    }
}
