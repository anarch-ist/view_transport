package ru.logist.sbat.jsonParser.beans;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.jsonParser.JSONReadFromFileTest;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class DataFrom1cTest {

    private static JSONObject jsonObject;

    @BeforeClass
    public static void setUp() throws Exception {
        Path jsonFile = Paths.get(JSONReadFromFileTest.class.getResource("EKA_third.pkg").toURI());
        jsonObject = JSONReadFromFile.read(jsonFile);
    }

    @Test
    public void testGetServer() throws Exception {
        DataFrom1c dataFrom1c = new DataFrom1c((JSONObject) jsonObject.get("dataFrom1C"));
        System.out.println(dataFrom1c.getServer());
    }

    @Test
    public void testGetPackageNumber() throws Exception {

    }

    @Test
    public void testGetCreated() throws Exception {

    }

    @Test
    public void testGetPackageData() throws Exception {

    }
}