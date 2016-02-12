package ru.logist.sbat.watchService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import static java.nio.file.StandardWatchEventKinds.*;
import java.util.List;

public class WatchServiceStarter {
    private static final Logger logger = LogManager.getLogger();

    private Path watchPath;
    private OnFileChangeListener onFileChangeListener;
    private volatile java.nio.file.WatchService watchService;

    public WatchServiceStarter(Path watchPath) {
        this.watchPath = watchPath;
        File watchPathAsFile = watchPath.toFile();
        if (!watchPathAsFile.exists() || !watchPathAsFile.isDirectory())
            throw new IllegalArgumentException("watchPath must be existing directory");
    }

    public void setOnFileChanged(OnFileChangeListener onFileChangeListener) {
        this.onFileChangeListener = onFileChangeListener;
    }

    public void doWatch() throws IOException {

        watchService = FileSystems.getDefault().newWatchService();
        logger.info("start watch service for [{}]", watchPath.toString());
        WatchKey watchKey = watchPath.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        while (!Thread.currentThread().isInterrupted()){

            WatchKey key;

            try {
                key = watchService.take();
            }
            catch (InterruptedException ex) {
                logger.error(ex);
                return;
            }

            List<WatchEvent<?>> eventList = key.pollEvents();

            for (WatchEvent<?> genericEvent: eventList) {

                WatchEvent.Kind<?> eventKind = genericEvent.kind();
                if (eventKind == OVERFLOW) {
                    continue; // pending events for loop
                }

                WatchEvent pathEvent = (WatchEvent) genericEvent;

                if (eventKind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                    Path createdFileName = watchPath.resolve((Path)pathEvent.context());
                    if (onFileChangeListener != null) {
                        onFileChangeListener.onFileCreate(createdFileName);
                    }
                }
                if (eventKind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                    Path deletedFileName = watchPath.resolve((Path)pathEvent.context());
                    if (onFileChangeListener != null) {
                        onFileChangeListener.onFileDelete(deletedFileName);
                    }
                }
                if (eventKind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                    Path modifiedFileName = watchPath.resolve((Path)pathEvent.context());
                    if (onFileChangeListener != null) {
                        onFileChangeListener.onFileModify(modifiedFileName);
                    }
                }

            }

            boolean validKey = key.reset();

            if (!validKey) {
                logger.error("Invalid key");
                break; // infinite for loop
            }

        } // end infinite for loop

        closeWatchService();
    }

    public void closeWatchService() {
        try {
            if (watchService != null)
                watchService.close();
        } catch (IOException e) {/*NOPE*/}
        logger.info("Watch service closed.");
    }

}