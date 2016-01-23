package ru.logist.sbat.jsonParser;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class JSONReadFromFile {

    public static JSONObject read(Path path) throws IOException, ParseException {
        return read(Files.newInputStream(path));
    }

    public static JSONObject read(InputStream jsonIn) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(jsonIn, "cp1251"))) {
            Object obj = parser.parse(in);
            return (JSONObject) obj;
        }
    }
}