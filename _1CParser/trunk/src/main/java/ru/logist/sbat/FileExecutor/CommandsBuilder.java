package ru.logist.sbat.FileExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.logist.sbat.db.DBManager;

import java.nio.file.Path;

public class CommandsBuilder {
    private static final Logger logger = LogManager.getLogger();

    // commands
    private FileToStringCmd fileToStringCmd;
    private JsonToBeanCmd jsonToBeanCmd;
    private BeanIntoDataBaseCmd beanIntoDataBaseCmd;
    private StringToJsonCmd stringToJsonCmd;
    private CopyToBackupCmd copyToBackupCmd;
    private WriteResponseCommand writeResponseCommand;
    private RemoveFileCommand removeFileCommand;

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

    public CommandsBuilder addFifthsCommand(WriteResponseCommand writeResponseCommand) {
        this.writeResponseCommand = writeResponseCommand;
        return this;
    }

    public CommandsBuilder addSixCommand(RemoveFileCommand removeFileCommand) {
        this.removeFileCommand = removeFileCommand;
        return this;
    }

    public void executeAll() throws CommandException {
        copyToBackupCmd.setFilePath(filePath);
        copyToBackupCmd.setBackupDir(backupDir);
        copyToBackupCmd.execute();

        fileToStringCmd.setFilePath(filePath);
        String fileAsString = fileToStringCmd.execute();

        stringToJsonCmd.setFileAsString(fileAsString);
        stringToJsonCmd.execute();

    }


}
