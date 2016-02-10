package ru.logist.sbat.watchService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class WatchServiceTest {

    private Path path;
    @Before
    public void setUp() throws Exception {
        path = Paths.get(WatchServiceStarter.class.getResource("testWatchDir").toURI());
        System.out.println(path);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
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