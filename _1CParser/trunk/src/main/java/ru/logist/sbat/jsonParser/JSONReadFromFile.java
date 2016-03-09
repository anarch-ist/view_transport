package ru.logist.sbat.jsonParser;


import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONReadFromFile {

    private static JSONObject readJson(Path path) throws IOException, ParseException {
        String jsonFileAsString = FileUtils.readFileToString(path.toFile(), StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        // removing BOM if exists
        Object obj = parser.parse(jsonFileAsString.replaceAll("\uFEFF", ""));
        return (JSONObject) obj;
    }

    public static DataFrom1c read(Path path) throws IOException, ParseException {
        return new DataFrom1c(readJson(path));
    }

}