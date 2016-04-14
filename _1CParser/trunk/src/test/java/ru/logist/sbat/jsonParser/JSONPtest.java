package ru.logist.sbat.jsonParser;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
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
        JsonReader reader = Json.createReader(new StringReader(string));
        JsonStructure jsonst = reader.read();
        jsonst.getValueType();
    }
}
