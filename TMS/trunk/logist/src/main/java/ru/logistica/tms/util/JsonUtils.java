package ru.logistica.tms.util;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class JsonUtils {
    public static JsonObject parseString(String string) {
        try (JsonReader receivedDataReader = Json.createReader(new StringReader(string))){
            return receivedDataReader.readObject();
        }
    }
}
