package ru.logist.sbat.jsonParser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONReadFromFileTest {

    Path jsonFile;

    @Before
    public void setUp() throws Exception {
        jsonFile = Paths.get(JSONReadFromFileTest.class.getResource("EKA_third.pkg").toURI());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRead() throws Exception {
        JSONReadFromFile.read(jsonFile);
    }
}
