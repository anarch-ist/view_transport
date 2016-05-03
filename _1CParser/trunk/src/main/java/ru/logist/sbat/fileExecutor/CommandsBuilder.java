package ru.logist.sbat.fileExecutor;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.db.TransactionResult;
import ru.logist.sbat.jsonToBean.beans.DataFrom1c;
import ru.logist.sbat.jsonToBean.jsonReader.JsonPException;
import ru.logist.sbat.jsonToBean.jsonReader.ValidatorException;

import java.io.IOException;
import java.nio.file.Path;

public class CommandsBuilder {
    private static final Logger logger = LogManager.getLogger();

    // commands
    private FileToStringCmd fileToStringCmd;
    private JsonToBeanCmd jsonToBeanCmd;
    private BeanIntoDataBaseCmd beanIntoDataBaseCmd;
    private StringToJsonCmd stringToJsonCmd;
    private CopyToBackupCmd copyToBackupCmd;
    private WriteResponseCmd writeResponseCommand;
    private RemoveFileCmd removeFileCommand;

    // resources
    private DBManager dbManager;
    private Path filePath;
    private Path responseDir;
    private Path backupDir;

    private int commandCounter;

    public CommandsBuilder setResources(DBManager dbManager, Path filePath, Path responseDir, Path backupDir) {
        this.dbManager = dbManager;
        this.filePath = filePath;
        this.responseDir = responseDir;
        this.backupDir = backupDir;
        return this;
    }

    public void executeAll() throws FatalException {

        commandCounter = 0;
        // copy incoming file to backup directory
        copyToBackupCmd = new CopyToBackupCmd(backupDir, filePath);
        try {
            copyToBackupCmd.execute();
        } catch (IOException e) {
            throw new FatalException("can't copy into backup directory", e);
        }

        commandCounter = 1;

        // read incoming file as string
        fileToStringCmd = new FileToStringCmd(filePath);
        String fileAsString;
        try {
            fileAsString = fileToStringCmd.execute();
        } catch (ValidatorException e) {
            logger.warn(e.getMessage());
            try {
                FileUtils.forceDelete(filePath.toFile());
                return;
            } catch (IOException ex) {
                throw new FatalException(ex);
            }
        } catch (IOException e) {
            throw new FatalException(e);
        }
        commandCounter = 2;



        // read string as JsonObject
        stringToJsonCmd = new StringToJsonCmd(fileAsString);
        JSONObject jsonObject = null;
        try {
            jsonObject = stringToJsonCmd.execute();
        } catch (JsonPException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        commandCounter = 3;

        // read JsonObject as bean object
        jsonToBeanCmd = new JsonToBeanCmd(jsonObject);
        DataFrom1c dataFrom1c = jsonToBeanCmd.execute();
        commandCounter = 4;

        // load data into database
        beanIntoDataBaseCmd = new BeanIntoDataBaseCmd(dataFrom1c, dbManager);
        TransactionResult transactionResult = beanIntoDataBaseCmd.execute();
        commandCounter = 5;

        // write response into response directory
        writeResponseCommand = new WriteResponseCmd(filePath, responseDir, transactionResult);
        writeResponseCommand.execute();
        commandCounter = 6;

        // remove incoming file
        removeFileCommand = new RemoveFileCmd(filePath);
        removeFileCommand.execute();
        commandCounter = 7;
    }

    public void cleanUp() throws CommandException {
        switch (commandCounter) {
            case 0: {
                //
            }

        }

    }
}
