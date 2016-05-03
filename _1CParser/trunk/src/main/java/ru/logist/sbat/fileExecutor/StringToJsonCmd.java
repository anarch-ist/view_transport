package ru.logist.sbat.fileExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToJsonCmd {
    private static final Logger logger = LogManager.getLogger();

    private String fileAsString;

    public StringToJsonCmd(String fileAsString) {
        this.fileAsString = fileAsString;
    }

    public JSONObject execute() throws JsonPException, ParseException {
        Objects.requireNonNull(fileAsString);
        return getJsonObjectFromString(fileAsString);
    }

    public JSONObject getJsonObjectFromString(String jsonFileAsString) throws ParseException, JsonPException {
        // идти по файлу используя regex и исправлять все ошибки
        String jsonFileAsStringWithoutBom = jsonFileAsString.replaceAll("\uFEFF", "");
        String fixedString = fixString(jsonFileAsStringWithoutBom);

        // TODO use JSONP here
        JsonReader reader = Json.createReader(new StringReader(fixedString));
        try {
            JsonStructure jsonst = reader.read();
        } catch (JsonException e) {
            throw new JsonPException(e);
        }

        // jsonst.getValueType();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(fixedString);
        return (JSONObject) obj;
    }

    protected String fixString(String jsonFileAsString) {
        Pattern pattern = Pattern.compile("(\"num_boxes\":\\s)(.*)(,)", Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(jsonFileAsString);
        StringBuffer stringBuffer = new StringBuffer();

        while (matcher.find()) {
            String forReplace = matcher.group(2);
            if (!forReplace.equals("\"\"") && !forReplace.matches("\\d+")) {
                String replacement = "\"\"";
                matcher.appendReplacement(stringBuffer, "$1" + replacement + "$3");
                logger.warn("num_boxes = [{}] is replaced with [{}]", forReplace, replacement);
            }
        }
        matcher.appendTail(stringBuffer);

        if (stringBuffer.length() > 0)
            return stringBuffer.toString();
        else
            return jsonFileAsString;
    }
}
