package ru.logist.sbat.fileExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.TransactionResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

public class WriteResponseCmd {
    public static final String RESPONSE_FILE_EXTENSION = ".ans";
    private static final Logger logger = LogManager.getLogger();
    private Path filePath;
    private Path responseDir;
    private TransactionResult transactionResult;

    public WriteResponseCmd(Path filePath, Path responseDir, TransactionResult transactionResult) {
        this.filePath = filePath;
        this.responseDir = responseDir;
        this.transactionResult = transactionResult;
    }

    public void execute() throws IOException {
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(responseDir);
        Objects.requireNonNull(transactionResult);

        if (transactionResult == null) {
            writeBadFileResponse(filePath);
        } else {
            writeDbWorkResponse(transactionResult);
        }
    }

    private void writeBadFileResponse(Path filePath) throws IOException {
        Path responsePath = responseDir.resolve(filePath.getFileName() + RESPONSE_FILE_EXTENSION);
        FileUtils.writeStringToFile(responsePath.toFile(), "Data format error . The details in the log file.", StandardCharsets.UTF_8);
    }

    private void writeDbWorkResponse(TransactionResult transactionResult) throws IOException {
        logger.info("Start write result data into response directory");
        Path responsePath = responseDir.resolve(transactionResult.getServer() + RESPONSE_FILE_EXTENSION);
        String resultString = transactionResult.toString();
        FileUtils.writeStringToFile(responsePath.toFile(), resultString, StandardCharsets.UTF_8);
        logger.info("Response data were successfully written");
    }
}
