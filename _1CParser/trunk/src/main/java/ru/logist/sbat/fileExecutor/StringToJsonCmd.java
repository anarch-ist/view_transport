package ru.logist.sbat.fileExecutor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.logist.sbat.jsonToBean.jsonReader.JsonPException;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import java.io.StringReader;
import java.util.Objects;

public class StringToJsonCmd implements Command<JSONObject> {
    private String fileAsString;

    public void setFileAsString(String fileAsString) {
        this.fileAsString = fileAsString;
    }

    @Override
    public JSONObject execute() throws CommandException {
        Objects.requireNonNull(fileAsString);
        try {
            return getJsonObjectFromString(fileAsString);
        } catch (ParseException | JsonPException e) {
            throw new CommandException(e);
        }
    }

    public JSONObject getJsonObjectFromString(String jsonFileAsString) throws ParseException, JsonPException {

        String jsonFileAsStringWithoutBom = jsonFileAsString.replaceAll("\uFEFF", "");
        // TODO use JSONP here

        JsonReader reader = Json.createReader(new StringReader(jsonFileAsStringWithoutBom));
        try {
            JsonStructure jsonst = reader.read();
        } catch (JsonException e) {
            throw new JsonPException(e);
        }

        // jsonst.getValueType();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonFileAsStringWithoutBom);
        return (JSONObject) obj;
    }
}
