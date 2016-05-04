package ru.logist.sbat.fileExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

public class WriteBadFileResponse {
    public static final String RESPONSE_FILE_EXTENSION = ".ans";
    private static final Logger logger = LogManager.getLogger();
    private Path filePath;
    private Path responseDir;

    public WriteBadFileResponse(Path filePath, Path responseDir) {
        this.filePath = filePath;
        this.responseDir = responseDir;
    }

    public void execute() throws IOException {
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(responseDir);
        writeBadFileResponse(filePath);
    }

    private void writeBadFileResponse(Path filePath) throws IOException {
        Path responsePath = responseDir.resolve(filePath.getFileName() + RESPONSE_FILE_EXTENSION);
        FileUtils.writeStringToFile(responsePath.toFile(), "Data format error . The details in the log file.", StandardCharsets.UTF_8);
    }
}
