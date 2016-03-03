package ru.logist.sbat.jsonParser;


import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONReadFromFile {

    public static JSONObject read(Path path) throws IOException, ParseException {
        String jsonFileAsString = FileUtils.readFileToString(path.toFile(), StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        // removing BOM if exists
        Object obj = parser.parse(jsonFileAsString.replaceAll("\uFEFF", ""));
        return (JSONObject) obj;
    }
}