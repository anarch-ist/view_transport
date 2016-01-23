package ru.logist.sbat.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesManager {

    private static Path prefsPath;

    public static Path getPrefsPath() {
        return prefsPath;
    }

    public static void setPrefsPath(Path prefsPath) {
        PropertiesManager.prefsPath = prefsPath;
    }

    public static Properties handleProperties() throws IOException {

        Properties properties = new Properties();
        if (!prefsPath.toFile().exists()) {
            DefaultProperties defaultProperties = new DefaultProperties();
            try (FileOutputStream fos = new FileOutputStream(prefsPath.toFile())) {
                defaultProperties.storeToXML(fos, "prefs", "UTF-8");
                return defaultProperties;
            }
        } else {
            properties.loadFromXML(new FileInputStream(prefsPath.toFile()));
            return properties;
        }
    }
}
