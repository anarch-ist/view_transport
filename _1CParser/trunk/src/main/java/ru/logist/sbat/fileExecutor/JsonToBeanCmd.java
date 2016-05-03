package ru.logist.sbat.fileExecutor;

import org.json.simple.JSONObject;
import ru.logist.sbat.jsonToBean.beans.DataFrom1c;
import ru.logist.sbat.jsonToBean.jsonReader.ValidatorException;

import java.util.Objects;

public class JsonToBeanCmd {
    private JSONObject jsonObject;

    public JsonToBeanCmd(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public DataFrom1c execute() throws ValidatorException {
        Objects.requireNonNull(jsonObject);
        return new DataFrom1c(jsonObject);
    }

}
