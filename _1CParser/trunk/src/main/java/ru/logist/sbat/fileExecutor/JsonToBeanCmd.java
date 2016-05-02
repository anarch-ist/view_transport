package ru.logist.sbat.fileExecutor;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonToBean.beans.DataFrom1c;
import ru.logist.sbat.jsonToBean.jsonReader.ValidatorException;

import java.util.Objects;

public class JsonToBeanCmd implements Command<DataFrom1c> {
    private JSONObject jsonObject;
    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public DataFrom1c execute() throws CommandException {
        Objects.requireNonNull(jsonObject);
        try {
            return new DataFrom1c(jsonObject);
        } catch (ValidatorException e) {
            throw new CommandException(e);
        }
    }

}
