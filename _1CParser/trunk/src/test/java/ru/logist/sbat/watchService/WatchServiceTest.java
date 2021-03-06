package ru.logist.sbat.watchService;

import org.junit.AfterClass;
import org.testng.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WatchServiceTest {

    private Path path;
    @BeforeClass
    public void setUp() throws Exception {
        path = Paths.get(WatchServiceStarter.class.getResource("testWatchDir").toURI());
    }

    @Test(enabled=false)
    public void testDoWatch() throws Exception {
        WatchServiceStarter watchServiceExample = new WatchServiceStarter(path);
        watchServiceExample.setOnFileChanged(new OnFileChangeListener() {
            @Override
            public void onFileCreate(Path filePath) {
                System.out.println(filePath);
            }
        });
        watchServiceExample.doWatch();
    }
}