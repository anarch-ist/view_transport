package ru.logistica.tms.util;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class JsonUtils {
    public static JsonObject parseStringAsObject(String string) {
        try (JsonReader receivedDataReader = Json.createReader(new StringReader(string))){
            return receivedDataReader.readObject();
        }
    }

    public static JsonArray parseStringAsArray(String string) {
        try (JsonReader receivedDataReader = Json.createReader(new StringReader(string))){
            return receivedDataReader.readArray();
        }
    }

}
