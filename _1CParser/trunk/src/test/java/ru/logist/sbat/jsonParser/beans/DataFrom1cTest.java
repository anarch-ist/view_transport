package ru.logist.sbat.jsonParser.beans;

import junit.framework.Assert;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.logist.sbat.jsonParser.JSONReadFromFile;
import ru.logist.sbat.jsonParser.JSONReadFromFileTest;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;

import static org.junit.Assert.*;

public class DataFrom1cTest {

    private static JSONObject jsonObject;
    private static DataFrom1c dataFrom1c;

    @BeforeClass
    public static void setUp() throws Exception {
        Path jsonFile = Paths.get(JSONReadFromFileTest.class.getResource("EKA_third.pkg").toURI());
        jsonObject = JSONReadFromFile.read(jsonFile);
        dataFrom1c = new DataFrom1c((JSONObject) jsonObject.get("dataFrom1C"));
    }

    @Test
    public void testGetServer() throws Exception {
        Assert.assertEquals("EKA", dataFrom1c.getServer());
    }

    @Test
    public void testGetPackageNumber() throws Exception {
        long packageNumber = dataFrom1c.getPackageNumber();
        Assert.assertEquals(packageNumber, 0);
    }

    @Test
    public void testGetCreated() throws Exception {
        Assert.assertEquals(dataFrom1c.getCreated().getTime(), 1456866000000L);
    }

    @Test
    public void testGetPackageData() throws Exception {

    }
}