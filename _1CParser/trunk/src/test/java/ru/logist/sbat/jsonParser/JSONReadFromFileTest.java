package ru.logist.sbat.jsonParser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JSONReadFromFileTest {

    private InputStream inputStream;
    private Path path;

    @Before
    public void setUp() throws Exception {
        URL resource = JSONReadFromFileTest.class.getResource("EKA_fixed.pkg");
        path = Paths.get(resource.toURI());
        inputStream = resource.openStream();

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRead() throws Exception {

        JSONObject jsonObject = JSONReadFromFile.read(inputStream);

        JSONObject dataFrom1C = (JSONObject) jsonObject.get("dataFrom1C");
        System.out.println(dataFrom1C.get("server"));
        System.out.println(dataFrom1C.get("packageNumber"));
        System.out.println(dataFrom1C.get("created"));

        JSONObject packageData = (JSONObject) dataFrom1C.get("packageData");
        JSONArray updatePointsArray = (JSONArray) packageData.get("updatePoints");
        System.out.println(updatePointsArray);

//        String name = (String) ((JSONObject) obj).get("Name");
//        String author = (String) ((JSONObject) obj).get("Author");
//        JSONArray companyList = (JSONArray) ((JSONObject) obj).get("Company List");
//
//        System.out.println("Name: " + name);
//        System.out.println("Author: " + author);
//        System.out.println("\nCompany List:");
//        Iterator<String> iterator = companyList.iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }

    }
}
