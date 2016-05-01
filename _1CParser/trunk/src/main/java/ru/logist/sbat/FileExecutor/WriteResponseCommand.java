package ru.logist.sbat.FileExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.TransactionResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class WriteResponseCommand implements Command<Void> {
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
        if (transactionResult == null) {
            writeBadFileResponse(filePath);
        } else {
            writeDbWorkResponse(transactionResult);
        }
        return null;
    }

    private void writeBadFileResponse(Path filePath) {
        Path responsePath = responseDir.resolve(filePath.getFileName() + RESPONSE_FILE_EXTENSION);
        try {
            FileUtils.writeStringToFile(responsePath.toFile(), "Data format error . The details in the log file.", StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Can't write response into [{}], exit application", responsePath);
            logger.error(e);
            System.exit(-1);
        }
    }

    private void writeDbWorkResponse(TransactionResult transactionResult) {
        logger.info("Start write result data into response directory");
        Path responsePath = responseDir.resolve(transactionResult.getServer() + RESPONSE_FILE_EXTENSION);
        String resultString = transactionResult.toString();
        try {
            FileUtils.writeStringToFile(responsePath.toFile(), resultString, StandardCharsets.UTF_8);
            logger.info("Response data were successfully written");
        } catch (IOException e) {
            logger.error("Can't write response into [{}], exit application", responsePath);
            logger.error(e);
            System.exit(-1);
        }
    }


}
