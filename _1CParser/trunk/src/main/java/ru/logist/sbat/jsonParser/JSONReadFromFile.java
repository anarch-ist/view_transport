package ru.logist.sbat.jsonParser;


import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.logist.sbat.jsonParser.beans.DataFrom1c;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        JSONParser parser = new JSONParser();
        // removing BOM if exists
        Object obj = parser.parse(jsonFileAsString.replaceAll("\uFEFF", ""));
        return (JSONObject) obj;
    }

}