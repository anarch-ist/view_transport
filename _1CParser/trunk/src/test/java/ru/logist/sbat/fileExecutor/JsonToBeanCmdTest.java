package ru.logist.sbat.fileExecutor;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.logist.sbat.testUtils.TestHelper;

import java.nio.file.Path;
import java.util.List;

public class JsonToBeanCmdTest {

    private List<Path> paths;
    private TestHelper testHelper;

    @BeforeClass
    public void setUp() throws Exception {

        //get list of all backup files
        testHelper = new TestHelper();
        paths = testHelper.listBackupFilesInOrder();
    }

    @Test
    public void testProcessRealFiles() throws Exception {
        for (Path path : paths) {
            System.out.println(path);
            testHelper.getBeanObjectFromFile(path);
        }
    }
}