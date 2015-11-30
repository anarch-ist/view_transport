package ru.logist.sbat;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class Controller extends BorderPane implements Initializable {
    private Properties properties;
    private Connection connection;

    private Properties handleProperties() throws IOException {
        Path userDir = Paths.get(System.getProperty("user.dir"));
        Path prefsPath = userDir.resolve("prefs.property");
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

    public Controller() {

        try {
            properties = handleProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create connection to database
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    properties.getProperty("url"),
                    properties.getProperty("user"),
                    properties.getProperty("password")
            );

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }



    }

    @FXML
    private ToggleButton startStopButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.getScene().getWindow().setOnCloseRequest(event -> {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        startStopButton.selectedProperty().addListener((observable, oldValue, isSelected) -> {
            if (isSelected) {
                // start new Thread(timer)
                //startInserts();


            } else {
                // interrupt timer thread
                //stopInserts();

            }

        });


    }
}
