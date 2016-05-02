package ru.logist.sbat.fileExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.TransactionResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;

public class WriteResponseCmd implements Command<Void> {
    public static final String RESPONSE_FILE_EXTENSION = ".ans";
    private static final Logger logger = LogManager.getLogger();
    private Path filePath;
    private Path responseDir;
    private TransactionResult transactionResult;

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }
    public void setResponseDir(Path responseDir) {
        this.responseDir = responseDir;
    }
    public void setTransactionResult(TransactionResult transactionResult) {
        this.transactionResult = transactionResult;
    }

    @Override
    public Void execute() throws CommandException {
        Objects.requireNonNull(filePath);
        Objects.requireNonNull(responseDir);
        Objects.requireNonNull(transactionResult);
        try {
            if (transactionResult == null) {
                writeBadFileResponse(filePath);
            } else {
                writeDbWorkResponse(transactionResult);
            }
        } catch (IOException e ) {
            throw new CommandException(e);
        }
        return null;
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
