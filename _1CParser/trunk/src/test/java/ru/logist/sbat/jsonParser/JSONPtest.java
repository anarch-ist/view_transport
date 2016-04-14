package ru.logist.sbat.jsonParser;

import org.apache.commons.io.FileUtils;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONPtest {
    Path jsonFile;
    private String string;

    @Before
    public void setUp() throws Exception {
        jsonFile = Paths.get(JSONReadFromFileTest.class.getResource("ULN/ULN.pkg").toURI());
        string = FileUtils.readFileToString(jsonFile.toFile(), StandardCharsets.UTF_8);
        string = string.replaceAll("\uFEFF", "");
    }

    @Test
    public void testRead() throws Exception {
        JsonParser parser = Json.createParser(new StringReader(string));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch(event) {
                case START_ARRAY:
                case END_ARRAY:
                case START_OBJECT:
                case END_OBJECT:
                case VALUE_FALSE:
                case VALUE_NULL:
                case VALUE_TRUE:
                    // System.out.println(event.toString());
                    break;
                case KEY_NAME:
//                    System.out.print(event.toString() + " " +
//                            parser.getString() + " - ");
                    break;
                case VALUE_STRING:
                case VALUE_NUMBER:
//                    System.out.println(event.toString() + " " +
//                            parser.getString());
                    break;
            }
        }
    }



}
