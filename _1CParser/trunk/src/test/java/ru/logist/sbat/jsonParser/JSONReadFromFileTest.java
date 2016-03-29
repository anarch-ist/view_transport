package ru.logist.sbat.jsonParser;

import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONReadFromFileTest {

    Path jsonFile;

    @Before
    public void setUp() throws Exception {
        jsonFile = Paths.get(JSONReadFromFileTest.class.getResource("EKA.pkg").toURI());
    }

    @Test
    public void testRead() throws Exception {
        JSONReadFromFile.read(jsonFile);
    }

    @Test(expected = ParseException.class)
    public void testReadWrongJsonFile() throws Exception {
        Path wrongJsonFormatFile = Paths.get(JSONReadFromFileTest.class.getResource("wrongJson.pkg").toURI());
        JSONReadFromFile.read(wrongJsonFormatFile);
    }

}
