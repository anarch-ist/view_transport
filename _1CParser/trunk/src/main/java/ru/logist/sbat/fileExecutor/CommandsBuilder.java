package ru.logist.sbat.fileExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import ru.logist.sbat.db.DBManager;
import ru.logist.sbat.db.TransactionResult;
import ru.logist.sbat.jsonToBean.beans.DataFrom1c;

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


    public CommandsBuilder setResources(DBManager dbManager, Path filePath, Path responseDir, Path backupDir) {
        this.dbManager = dbManager;
        this.filePath = filePath;
        this.responseDir = responseDir;
        this.backupDir = backupDir;
        return this;
    }

    public CommandsBuilder addZeroCommand(CopyToBackupCmd copyToBackupCmd) {
        this.copyToBackupCmd = copyToBackupCmd;
        return this;
    }


    public CommandsBuilder addFirstCommand(FileToStringCmd fileToStringCmd) {
        this.fileToStringCmd = fileToStringCmd;
        return this;
    }

    public CommandsBuilder addSecondCommand(StringToJsonCmd stringToJsonCmd) {
        this.stringToJsonCmd = stringToJsonCmd;
        return this;
    }

    public CommandsBuilder addThirdCommand(JsonToBeanCmd jsonToBeanCmd) {
        this.jsonToBeanCmd = jsonToBeanCmd;
        return this;
    }

    public CommandsBuilder addFourthCommand(BeanIntoDataBaseCmd beanIntoDataBaseCmd) {
        this.beanIntoDataBaseCmd = beanIntoDataBaseCmd;
        return this;
    }

    public CommandsBuilder addFifthsCommand(WriteResponseCmd writeResponseCommand) {
        this.writeResponseCommand = writeResponseCommand;
        return this;
    }

    public CommandsBuilder addSixCommand(RemoveFileCmd removeFileCommand) {
        this.removeFileCommand = removeFileCommand;
        return this;
    }

    public void executeAll() throws CommandException {

        // copy incoming file to backup directory
        copyToBackupCmd.setFilePath(filePath);
        copyToBackupCmd.setBackupDir(backupDir);
        copyToBackupCmd.execute();

        // read incoming file as string
        fileToStringCmd.setFilePath(filePath);
        String fileAsString = fileToStringCmd.execute();

        // read string as JsonObject
        stringToJsonCmd.setFileAsString(fileAsString);
        JSONObject jsonObject = stringToJsonCmd.execute();

        // read JsonObject as bean object
        jsonToBeanCmd.setJsonObject(jsonObject);
        DataFrom1c dataFrom1c = jsonToBeanCmd.execute();

        // load data into database
        beanIntoDataBaseCmd.setDataFrom1c(dataFrom1c);
        beanIntoDataBaseCmd.setDbManager(dbManager);
        TransactionResult transactionResult = beanIntoDataBaseCmd.execute();

        // write response into response directory
        writeResponseCommand.setTransactionResult(transactionResult);
        writeResponseCommand.setFilePath(filePath);
        writeResponseCommand.setResponseDir(responseDir);
        writeResponseCommand.execute();

        // remove incoming file
        removeFileCommand.setFilePath(filePath);
        removeFileCommand.execute();
    }
}
