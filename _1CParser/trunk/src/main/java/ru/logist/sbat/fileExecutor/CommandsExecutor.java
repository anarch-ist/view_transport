package ru.logist.sbat.fileExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import ru.logist.sbat.db.DBCohesionException;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.db.TransactionResult;
import ru.logist.sbat.jsonToBean.beans.DataFrom1c;
import ru.logist.sbat.jsonToBean.jsonReader.JsonPException;
import ru.logist.sbat.jsonToBean.jsonReader.ValidatorException;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

public class CommandsExecutor {
    private static final Logger logger = LogManager.getLogger();

    private WriteBadFileResponse writeBadFileResponse;
    private RemoveFileCmd removeFileCommand;

    // resources
    private DBManager dbManager;
    private Path filePath;
    private Path responseDir;
    private Path backupDir;

    public CommandsExecutor(DBManager dbManager, Path filePath, Path responseDir, Path backupDir) {
        this.dbManager = dbManager;
        this.filePath = filePath;
        this.responseDir = responseDir;
        this.backupDir = backupDir;

        removeFileCommand = new RemoveFileCmd(filePath);
        writeBadFileResponse = new WriteBadFileResponse(filePath, responseDir);
    }

    public void executeAll() throws FatalException {

        // copy incoming file to backup directory
        CopyToBackupCmd copyToBackupCmd = new CopyToBackupCmd(backupDir, filePath);
        try {
            copyToBackupCmd.execute();
        } catch (IOException e) {
            throw new FatalException("can't copy into backup directory", e);
        }

        // read incoming file as string
        FileToStringCmd fileToStringCmd = new FileToStringCmd(filePath);
        String fileAsString;
        try {
            fileAsString = fileToStringCmd.execute();
        } catch (ValidatorException e) {
            logger.error(e);
            deleteIncomingFile();
            writeBadFileResponse();
            return;
        } catch (IOException e) {
            throw new FatalException(e);
        }

        // read string as JsonObject
        StringToJsonCmd stringToJsonCmd = new StringToJsonCmd(fileAsString);
        JSONObject jsonObject;
        try {
            jsonObject = stringToJsonCmd.execute();
        } catch (JsonPException|ParseException  e) {
            logger.error(e);
            deleteIncomingFile();
            writeBadFileResponse();
            return;
        }

        // read JsonObject as bean object
        JsonToBeanCmd jsonToBeanCmd = new JsonToBeanCmd(jsonObject);
        DataFrom1c dataFrom1c;
        try {
            dataFrom1c = jsonToBeanCmd.execute();
        } catch (ValidatorException e) {
            logger.error(e);
            deleteIncomingFile();
            writeBadFileResponse();
            return;
        }

        // load data into database
        BeanIntoDataBaseCmd beanIntoDataBaseCmd = new BeanIntoDataBaseCmd(dataFrom1c, dbManager);
        TransactionResult transactionResult = new TransactionResult();
        transactionResult.setServer(dataFrom1c.getServer());
        transactionResult.setPackageNumber(dataFrom1c.getPackageNumber().intValue());

        try {
            beanIntoDataBaseCmd.execute();
            transactionResult.setStatus(TransactionResult.OK_STATUS);
        } catch (DBCohesionException e) {
            logger.warn(e);
            rollbackWithFatalEx();
            deleteIncomingFile();
            transactionResult.setStatus(TransactionResult.ERROR_STATUS);
            writeDbFileResponse(transactionResult);
            return;
        } catch (SQLException e) {
            logger.error(e);
            rollbackWithFatalEx();
            deleteIncomingFile();
            transactionResult.setStatus(TransactionResult.ERROR_STATUS);
            writeDbFileResponse(transactionResult);
            return;
        }

        // write response into response directory
        writeDbFileResponse(transactionResult);
        // remove incoming file
        deleteIncomingFile();
    }

    private void rollbackWithFatalEx() throws FatalException {
        try {
            dbManager.getConnection().rollback();
        } catch (SQLException ex) {
            throw new FatalException(ex);
        }
    }


    private void deleteIncomingFile() throws FatalException {
        try {
            removeFileCommand.execute();
        } catch (IOException ex) {
            throw new FatalException(ex);
        }
    }

    private void writeBadFileResponse() throws FatalException {
        try {
            writeBadFileResponse.execute();
        } catch (IOException e) {
            throw new FatalException(e);
        }
    }

    private void writeDbFileResponse(TransactionResult transactionResult) throws FatalException {
        WriteDbWorkResponseCmd writeDbWorkResponseCmd = new WriteDbWorkResponseCmd(filePath, responseDir, transactionResult);
        try {
            writeDbWorkResponseCmd.execute();
        } catch (IOException e) {
            throw new FatalException(e);
        }
    }


}
