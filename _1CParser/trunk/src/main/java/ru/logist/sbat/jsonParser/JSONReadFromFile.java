package ru.logist.sbat.jsonParser;


import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger();

    private static final String INCOMING_FILE_EXTENSION_ZIP = ".zip";
    private static final String INCOMING_FILE_EXTENSION_PKG = ".pkg";

    protected static DataFrom1c readZip(Path path) throws IOException, ParseException, ValidatorException, JsonPException {
        String jsonFileAsString = readZipFileToUtf8String(path.toFile());
        return new DataFrom1c(getJsonObjectFromString(jsonFileAsString));
    }

    private static DataFrom1c readPkg(Path path) throws IOException, ParseException, ValidatorException, JsonPException {
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

    public static JSONObject getJsonObjectFromString(String jsonFileAsString) throws ParseException, JsonPException {

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

    /**
     *
     * @param filePath
     * @return null if file was not imported
     * @throws ValidatorException
     * @throws JsonPException
     * @throws ParseException
     * @throws IOException
     */
    public static DataFrom1c getJsonObjectFromFile(Path filePath) throws ValidatorException, JsonPException, ParseException, IOException {
        DataFrom1c result = null;
        if (filePath.toString().endsWith(INCOMING_FILE_EXTENSION_ZIP)) {
            result = JSONReadFromFile.readZip(filePath);
            logger.info("Start creating dataFrom1c object from file [{}]", filePath);
        } else if (filePath.toString().endsWith(INCOMING_FILE_EXTENSION_PKG)) {
            result = JSONReadFromFile.readPkg(filePath);
            logger.info("Start creating dataFrom1c object from file [{}]", filePath);
        } else {
            logger.warn("file [{}] must end with [{}] or [{}] ,file will not be imported", filePath, INCOMING_FILE_EXTENSION_ZIP, INCOMING_FILE_EXTENSION_PKG);
        }
        return result;
    }

}