package ru.logist.sbat.jsonParser.jsonReader;

import org.apache.commons.io.FileUtils;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.stream.JsonParsingException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONReadFromFileTest {

    @Test
    public void testZipRead() throws Exception {

        Path jsonDir = Paths.get(JSONReadFromFileTest.class.getResource("backup").toURI());
        String[] files = jsonDir.toFile().list();
        for (String fileName : files) {
            Path filePath = jsonDir.resolve(fileName);
            //String fileToString = FileUtils.readFileToString(filePath.toFile(), StandardCharsets.UTF_8);
            String fileToUtf8String = JSONReadFromFile.readZipFileToUtf8String(filePath.toFile());
            String jsonFileAsStringWithoutBom = fileToUtf8String.replaceAll("\uFEFF", "");

            JsonReader reader = Json.createReader(new StringReader(jsonFileAsStringWithoutBom));
            try {
                JsonStructure jsonst = reader.read();
            } catch (JsonParsingException e) {
                System.out.println(fileName);
                System.out.println(e.getMessage());
                System.out.println("_________________");
            }


//            try {
//                JSONReadFromFile.readZip(filePath);
//            } catch (JsonPException e) {
//                System.out.println(fileName);
//                System.out.println(e.getMessage());
//                System.out.println("_________________");
//            }

        }
//        Path jsonFile = Paths.get(JSONReadFromFileTest.class.getResource("backup/_2016-04-18_21-04-48_IRK.zip").toURI());
//        JSONReadFromFile.readZip(jsonFile);
    }

    @Test(expected = ParseException.class)
    public void testReadWrongJsonFile() throws Exception {
        Path wrongJsonFormatFile = Paths.get(JSONReadFromFileTest.class.getResource("wrongJson.pkg").toURI());
        JSONReadFromFile.getJsonObjectFromFile(wrongJsonFormatFile);
    }

    @Test
    public void testReadWithStreamingApi() throws Exception {
        Path jsonFile = Paths.get(JSONReadFromFileTest.class.getResource("template.pkg").toURI());
        JSONReadFromFile.readWithStreamingApi(FileUtils.readFileToString(jsonFile.toFile(), StandardCharsets.UTF_8));
    }
}
