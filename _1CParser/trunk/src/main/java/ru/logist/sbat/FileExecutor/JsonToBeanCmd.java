package ru.logist.sbat.FileExecutor;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;
import ru.logist.sbat.jsonParser.jsonReader.ValidatorException;

import java.util.Objects;

public class JsonToBeanCmd implements Command<DataFrom1c> {
    private JSONObject jsonObject;

    @Override
    public DataFrom1c execute() throws CommandException {
        Objects.requireNonNull(jsonObject);
        try {
            return new DataFrom1c(jsonObject);
        } catch (ValidatorException e) {
            throw new CommandException(e);
        }
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
