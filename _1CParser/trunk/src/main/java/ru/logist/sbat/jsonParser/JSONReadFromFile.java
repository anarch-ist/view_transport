package ru.logist.sbat.jsonParser;


import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.stream.JsonParsingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.zip.ZipInputStream;

public class JSONReadFromFile {

    public static DataFrom1c readZip(Path path) throws IOException, ParseException {
        String jsonFileAsString = readZipFileToUtf8String(path.toFile());
        return new DataFrom1c(getJsonObjectFromString(jsonFileAsString));
    }

    public static DataFrom1c readPkg(Path path) throws IOException, ParseException {
        String jsonFileAsString = FileUtils.readFileToString(path.toFile(), StandardCharsets.UTF_8);
        return new DataFrom1c(getJsonObjectFromString(jsonFileAsString));
    }

    /**
     *
     * @return zip file with data as decompressed string
     */
    private static String readZipFileToUtf8String(File file) throws IOException {
        byte[] buffer = new byte[2048];
        ZipInputStream zis = null;
        try {
            zis = new ZipInputStream(new FileInputStream(file));
            // in our file should be only one entry
            zis.getNextEntry(); // get .pkg entry
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            int len;
            while ((len = zis.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
            return output.toString("UTF-8");
        }
        finally {
            if (zis != null) zis.close();
        }
    }

    private static JSONObject getJsonObjectFromString(String jsonFileAsString) throws ParseException {

        String jsonFileAsStringWithoutBom = jsonFileAsString.replaceAll("\uFEFF", "");
        // TODO use JSONP here

        JsonReader reader = Json.createReader(new StringReader(jsonFileAsStringWithoutBom));
        JsonStructure jsonst = reader.read();
        // jsonst.getValueType();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonFileAsStringWithoutBom);
        return (JSONObject) obj;
    }



}